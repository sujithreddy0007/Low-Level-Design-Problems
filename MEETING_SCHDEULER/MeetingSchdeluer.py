class User:
    
    def __init__(self, user_id, name):
        self.user_id = user_id
        self.name = name


class Meeting:

    def __init__(self,
                 meeting_id,
                 title,
                 organizer,
                 start_time,
                 end_time,
                 participants):

        self.meeting_id = meeting_id
        self.title = title

        self.organizer = organizer

        self.start_time = int(start_time)
        self.end_time = int(end_time)

        self.participants = participants

    def overlaps(self, other):

        return max(
            self.start_time,
            other.start_time
        ) < min(
            self.end_time,
            other.end_time
        )

    def __str__(self):

        return (
            f"Meeting("
            f"id={self.meeting_id}, "
            f"title={self.title}, "
            f"start={self.start_time}, "
            f"end={self.end_time})"
        )


class MeetingScheduler:

    def __init__(self):

        self.users = {}

        self.meetings = {}

        self.user_meetings = {}

        self.meeting_counter = 1

    def add_user(self, user):

        self.users[user.user_id] = user

        self.user_meetings[user.user_id] = []

    def schedule_meeting(
            self,
            title,
            organizer_id,
            participant_ids,
            start_time,
            end_time):

        organizer = self.users.get(
            organizer_id
        )

        if not organizer:
            return None

        participants = [organizer]

        for user_id in participant_ids:

            user = self.users.get(user_id)

            if user:
                participants.append(user)

        meeting_id = (
            f"M{self.meeting_counter}"
        )

        self.meeting_counter += 1

        new_meeting = Meeting(
            meeting_id,
            title,
            organizer,
            start_time,
            end_time,
            participants
        )

        for user in participants:

            existing_meetings = (
                self.user_meetings[
                    user.user_id
                ]
            )

            for meeting in existing_meetings:

                if meeting.overlaps(
                        new_meeting):
                    return None

        self.meetings[
            meeting_id
        ] = new_meeting

        for user in participants:

            self.user_meetings[
                user.user_id
            ].append(new_meeting)

        return new_meeting

    def cancel_meeting(
            self,
            meeting_id):

        meeting = self.meetings.get(
            meeting_id
        )

        if not meeting:
            return False

        for user in meeting.participants:

            self.user_meetings[
                user.user_id
            ].remove(meeting)

        del self.meetings[
            meeting_id
        ]

        return True

    def get_meetings(
            self,
            user_id):

        return self.user_meetings.get(
            user_id,
            []
        )


# ----------------------------
# Driver Code
# ----------------------------

scheduler = MeetingScheduler()

u1 = User("U1", "Sai")
u2 = User("U2", "Rahul")
u3 = User("U3", "Ravi")

scheduler.add_user(u1)
scheduler.add_user(u2)
scheduler.add_user(u3)

m1 = scheduler.schedule_meeting(
    "Design Discussion",
    "U1",
    ["U2", "U3"],
    "10",
    "12"
)

print(m1)

m2 = scheduler.schedule_meeting(
    "System Design",
    "U2",
    ["U3"],
    "11",
    "13"
)

print(m2)

print("\nMeetings of U3:")
for meeting in scheduler.get_meetings("U3"):
    print(meeting)

scheduler.cancel_meeting(
    m1.meeting_id
)

print("\nAfter cancellation:")

for meeting in scheduler.get_meetings("U3"):
    print(meeting)