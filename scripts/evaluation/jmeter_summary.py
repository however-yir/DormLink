#!/usr/bin/env python3
"""Summarize JMeter CSV results for DormLink API latency metrics."""

import csv
import math
import sys
from pathlib import Path
from statistics import mean


def percentile(values: list[float], p: float) -> float:
    if not values:
        return 0.0
    if len(values) == 1:
        return values[0]
    ordered = sorted(values)
    rank = (len(ordered) - 1) * p
    low = math.floor(rank)
    high = math.ceil(rank)
    if low == high:
        return ordered[low]
    weight = rank - low
    return ordered[low] * (1 - weight) + ordered[high] * weight


def summarize(csv_path: Path) -> dict[str, float]:
    elapsed_ms: list[float] = []
    success_count = 0
    total_count = 0

    with csv_path.open("r", encoding="utf-8") as file:
        reader = csv.DictReader(file)
        for row in reader:
            total_count += 1
            elapsed = float(row.get("elapsed", 0))
            elapsed_ms.append(elapsed)
            if str(row.get("success", "")).lower() == "true":
                success_count += 1

    if total_count == 0:
        return {
            "samples": 0,
            "success_rate": 0.0,
            "avg_ms": 0.0,
            "p95_ms": 0.0,
            "p99_ms": 0.0,
        }

    return {
        "samples": float(total_count),
        "success_rate": success_count / total_count,
        "avg_ms": mean(elapsed_ms),
        "p95_ms": percentile(elapsed_ms, 0.95),
        "p99_ms": percentile(elapsed_ms, 0.99),
    }


def main() -> None:
    if len(sys.argv) != 2:
        print("Usage: python3 scripts/evaluation/jmeter_summary.py <jmeter-jtl-csv>")
        return

    csv_path = Path(sys.argv[1])
    if not csv_path.exists():
        print(f"JMeter result file not found: {csv_path}")
        return

    metrics = summarize(csv_path)
    print("DormLink JMeter Summary")
    print(f"Samples={int(metrics['samples'])}")
    print(f"SuccessRate={metrics['success_rate']:.4f}")
    print(f"AvgMs={metrics['avg_ms']:.2f}")
    print(f"P95Ms={metrics['p95_ms']:.2f}")
    print(f"P99Ms={metrics['p99_ms']:.2f}")


if __name__ == "__main__":
    main()
