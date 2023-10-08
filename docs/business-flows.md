# DormLink Business Flows

## 0. Multi-Role Closed-Loop Overview

- Student: submit check-in, room adjustment, and repair requests.
- Dorm manager: review requests and process tickets.
- System: enforce state transitions and persist timestamps.
- Closed-loop entry: student request submission.
- Closed-loop exit: approved/rejected result with traceable timestamps.

## 1. Check-In Workflow

```mermaid
flowchart TD
  A[Student submits check-in request] --> B{Required identity and room info valid?}
  B -- No --> X[Reject request]
  B -- Yes --> C[Create record with state=待审核]
  C --> D[Dorm manager reviews materials]
  D --> E{Approve?}
  E -- No --> F[Set state=驳回 and record reason]
  E -- Yes --> G[Allocate room and bed]
  G --> H[Set state=已入住 and checkin_time]
  H --> I[Notify student and archive process]
```

## 2. Repair Workflow

```mermaid
flowchart TD
  A[Student submits repair request] --> B{Required fields valid?}
  B -- No --> X[Reject request]
  B -- Yes --> C[Create ticket]
  C --> D[Default state = 未完成]
  D --> E[Set order_buildtime]
  E --> F[Dorm manager processes ticket]
  F --> G{Mark as 完成?}
  G -- No --> H[Keep order_finishtime null]
  G -- Yes --> I[Auto-fill order_finishtime]
  I --> J{finish >= build?}
  J -- No --> X
  J -- Yes --> K[Persist update]
```

## 3. Room Adjustment Workflow

```mermaid
flowchart TD
  A[Student creates adjust request] --> B{Has same pending request?}
  B -- Yes --> X[Reject duplicate]
  B -- No --> C[Set state=未处理 and apply_time]
  C --> D[Dorm manager reviews]
  D --> E{Approve?}
  E -- No --> F[Set state=驳回]
  E -- Yes --> G[Move bed assignment]
  G --> H[Set state=通过 and finish_time]
```

## 4. State and Handover Rules

### 4.1 Check-In State Set

- `待审核`: request created and waiting dorm manager review.
- `驳回`: material not qualified, student can resubmit.
- `已入住`: room and bed assigned, process closed.

### 4.2 Repair State Set

- `未完成`: ticket accepted and pending process.
- `完成`: processing finished with valid finish timestamp.

### 4.3 Adjustment State Set

- `未处理`: request accepted and pending manager action.
- `驳回`: manager rejected the adjustment.
- `通过`: bed migration completed and timestamp recorded.
