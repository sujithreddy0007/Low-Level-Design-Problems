import java.util.*;

class User {

    private String userId;
    private String name;

    public User(String userId,
                String name) {

        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}

class Meeting {

    private String meetingId;
    private String title;

    private User organizer;

    private int startTime;
    private int endTime;

    private List<User> participants;

    public Meeting(String meetingId,
                   String title,
                   User organizer,
                   String startTime,
                   String endTime,
                   List<User> participants) {

        this.meetingId = meetingId;
        this.title = title;
        this.organizer = organizer;

        this.startTime = Integer.parseInt(startTime);
        this.endTime = Integer.parseInt(endTime);

        this.participants = participants;
    }

    public boolean overlaps(Meeting other) {

        return Math.max(
                this.startTime,
                other.startTime
        ) < Math.min(
                this.endTime,
                other.endTime
        );
    }

    public String getMeetingId() {
        return meetingId;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {

        return "Meeting{" +
                "meetingId='" + meetingId + '\'' +
                ", title='" + title + '\'' +
                ", start=" + startTime +
                ", end=" + endTime +
                '}';
    }
}

class MeetingScheduler {

    private Map<String, User> users;

    private Map<String, Meeting> meetings;

    private Map<String, List<Meeting>> userMeetings;

    private int meetingCounter;

    public MeetingScheduler() {

        users = new HashMap<>();

        meetings = new HashMap<>();

        userMeetings = new HashMap<>();

        meetingCounter = 1;
    }

    public void addUser(User user) {

        users.put(
                user.getUserId(),
                user
        );

        userMeetings.put(
                user.getUserId(),
                new ArrayList<>()
        );
    }

    public Meeting scheduleMeeting(
            String title,
            String organizerId,
            List<String> participantIds,
            String startTime,
            String endTime) {

        User organizer =
                users.get(organizerId);

        if (organizer == null) {
            return null;
        }

        List<User> participants =
                new ArrayList<>();

        participants.add(organizer);

        for (String id : participantIds) {

            User user = users.get(id);

            if (user != null) {
                participants.add(user);
            }
        }

        String meetingId =
                "M" + meetingCounter++;

        Meeting newMeeting =
                new Meeting(
                        meetingId,
                        title,
                        organizer,
                        startTime,
                        endTime,
                        participants
                );

        for (User user : participants) {

            List<Meeting> existingMeetings =
                    userMeetings.get(
                            user.getUserId()
                    );

            for (Meeting meeting :
                    existingMeetings) {

                if (meeting.overlaps(
                        newMeeting)) {

                    return null;
                }
            }
        }

        meetings.put(
                meetingId,
                newMeeting
        );

        for (User user : participants) {

            userMeetings.get(
                    user.getUserId()
            ).add(newMeeting);
        }

        return newMeeting;
    }

    public boolean cancelMeeting(
            String meetingId) {

        Meeting meeting =
                meetings.get(meetingId);

        if (meeting == null) {
            return false;
        }

        for (User user :
                meeting.getParticipants()) {

            userMeetings.get(
                    user.getUserId()
            ).remove(meeting);
        }

        meetings.remove(meetingId);

        return true;
    }

    public List<Meeting> getMeetings(
            String userId) {

        return userMeetings.getOrDefault(
                userId,
                new ArrayList<>()
        );
    }
}

public class Main {

    public static void main(String[] args) {

        MeetingScheduler scheduler =
                new MeetingScheduler();

        User u1 =
                new User("U1", "Sai");

        User u2 =
                new User("U2", "Rahul");

        User u3 =
                new User("U3", "Ravi");

        scheduler.addUser(u1);
        scheduler.addUser(u2);
        scheduler.addUser(u3);

        Meeting m1 =
                scheduler.scheduleMeeting(
                        "Design Discussion",
                        "U1",
                        Arrays.asList(
                                "U2",
                                "U3"
                        ),
                        "10",
                        "12"
                );

        System.out.println(m1);

        Meeting m2 =
                scheduler.scheduleMeeting(
                        "System Design",
                        "U2",
                        Arrays.asList(
                                "U3"
                        ),
                        "11",
                        "13"
                );

        System.out.println(m2);

        System.out.println(
                scheduler.getMeetings("U3")
        );

        scheduler.cancelMeeting(
                m1.getMeetingId()
        );

        System.out.println(
                scheduler.getMeetings("U3")
        );
    }
}