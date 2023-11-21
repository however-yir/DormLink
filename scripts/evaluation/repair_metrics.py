#!/usr/bin/env python3
"""Compute offline KPI metrics for DormLink repair workflow."""

import json
from dataclasses import dataclass
from datetime import datetime
from pathlib import Path
from statistics import mean

TIME_FORMAT = "%Y-%m-%d %H:%M:%S"


@dataclass
class RepairRecord:
    ticket_id: int
    state: str
    reported_at: datetime
    finished_at: datetime | None
    sla_hours: float
    feedback_score: int | None


def parse_time(value: str | None) -> datetime | None:
    if not value:
        return None
    return datetime.strptime(value, TIME_FORMAT)


def load_records(path: Path) -> list[RepairRecord]:
    rows = json.loads(path.read_text(encoding="utf-8"))
    records: list[RepairRecord] = []
    for row in rows:
        records.append(
            RepairRecord(
                ticket_id=int(row["id"]),
                state=row["state"],
                reported_at=parse_time(row["reported_at"]),
                finished_at=parse_time(row.get("finished_at")),
                sla_hours=float(row["sla_hours"]),
                feedback_score=row.get("feedback_score"),
            )
        )
    return records


def hours_between(start: datetime, end: datetime) -> float:
    return round((end - start).total_seconds() / 3600.0, 4)


def compute_metrics(records: list[RepairRecord]) -> dict[str, float]:
    if not records:
        return {
            "total_tickets": 0,
            "completion_rate": 0.0,
            "on_time_completion_rate": 0.0,
            "avg_completion_hours": 0.0,
            "pending_overdue_rate": 0.0,
            "avg_feedback_score": 0.0,
        }

    completed = [r for r in records if r.state == "完成" and r.finished_at is not None]
    pending = [r for r in records if r.state != "完成"]

    completed_durations = [hours_between(r.reported_at, r.finished_at) for r in completed]
    on_time_count = sum(
        1
        for r, duration in zip(completed, completed_durations)
        if duration <= r.sla_hours
    )

    # For offline samples we evaluate pending overdue against a fixed observation point
    snapshot_time = max(r.reported_at for r in records)
    pending_overdue_count = sum(
        1
        for r in pending
        if hours_between(r.reported_at, snapshot_time) > r.sla_hours
    )

    feedback_scores = [r.feedback_score for r in completed if r.feedback_score is not None]

    total = len(records)
    return {
        "total_tickets": float(total),
        "completion_rate": len(completed) / total,
        "on_time_completion_rate": (on_time_count / len(completed)) if completed else 0.0,
        "avg_completion_hours": mean(completed_durations) if completed_durations else 0.0,
        "pending_overdue_rate": (pending_overdue_count / len(pending)) if pending else 0.0,
        "avg_feedback_score": mean(feedback_scores) if feedback_scores else 0.0,
    }


def main() -> None:
    data_path = Path("evaluation/repair_kpi_dataset.sample.json")
    if not data_path.exists():
        print(f"dataset not found: {data_path}")
        return

    records = load_records(data_path)
    metrics = compute_metrics(records)

    print("DormLink Repair KPI (offline sample)")
    print(f"TotalTickets={int(metrics['total_tickets'])}")
    print(f"CompletionRate={metrics['completion_rate']:.4f}")
    print(f"OnTimeCompletionRate={metrics['on_time_completion_rate']:.4f}")
    print(f"AvgCompletionHours={metrics['avg_completion_hours']:.2f}")
    print(f"PendingOverdueRate={metrics['pending_overdue_rate']:.4f}")
    print(f"AvgFeedbackScore={metrics['avg_feedback_score']:.2f}")


if __name__ == "__main__":
    main()
