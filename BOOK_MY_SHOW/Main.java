import java.util.*;

class Movie {

    private String movieId;
    private String title;
    private int durationInMinutes;

    public Movie(String movieId,
                 String title,
                 int durationInMinutes) {

        this.movieId = movieId;
        this.title = title;
        this.durationInMinutes = durationInMinutes;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }
}

class Seat {

    private String seatId;

    public Seat(String seatId) {
        this.seatId = seatId;
    }

    public String getSeatId() {
        return seatId;
    }

    @Override
    public String toString() {
        return seatId;
    }
}

class Screen {

    private String screenId;
    private List<Seat> seats;

    public Screen(String screenId,
                  List<Seat> seats) {

        this.screenId = screenId;
        this.seats = seats;
    }

    public String getScreenId() {
        return screenId;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}

class Show {

    private String showId;
    private Movie movie;
    private Screen screen;
    private String startTime;

    // Stores booked seat ids
    private Set<String> bookedSeats;

    public Show(String showId,
                Movie movie,
                Screen screen,
                String startTime) {

        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;

        bookedSeats = new HashSet<>();
    }

    public String getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public boolean bookSeats(List<String> seatIds) {

        for (String seatId : seatIds) {
            if (bookedSeats.contains(seatId)) {
                return false;
            }
        }

        bookedSeats.addAll(seatIds);

        return true;
    }

    public void cancelSeats(List<String> seatIds) {

        for (String seatId : seatIds) {
            bookedSeats.remove(seatId);
        }
    }

    public List<Seat> getAvailableSeats() {

        List<Seat> availableSeats = new ArrayList<>();

        for (Seat seat : screen.getSeats()) {

            if (!bookedSeats.contains(seat.getSeatId())) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }

    public Screen getScreen() {
        return screen;
    }
}

class Theatre {

    private String theatreId;
    private String city;

    private List<Show> shows;

    public Theatre(String theatreId,
                   String city) {

        this.theatreId = theatreId;
        this.city = city;

        shows = new ArrayList<>();
    }

    public String getTheatreId() {
        return theatreId;
    }

    public String getCity() {
        return city;
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    public List<Show> getShows() {
        return shows;
    }
}

class Booking {

    private String bookingId;
    private String userId;
    private Show show;
    private List<Seat> seats;

    public Booking(String bookingId,
                   String userId,
                   Show show,
                   List<Seat> seats) {

        this.bookingId = bookingId;
        this.userId = userId;
        this.show = show;
        this.seats = seats;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}

class BookMyShow {

    private Map<String, Movie> movies;
    private Map<String, Theatre> theatres;
    private Map<String, Show> shows;
    private Map<String, Booking> bookings;

    private int bookingCounter;

    public BookMyShow() {

        movies = new HashMap<>();
        theatres = new HashMap<>();
        shows = new HashMap<>();
        bookings = new HashMap<>();

        bookingCounter = 1;
    }

    public void addMovie(Movie movie) {
        movies.put(movie.getMovieId(), movie);
    }

    public void addTheatre(Theatre theatre) {
        theatres.put(theatre.getTheatreId(), theatre);
    }

    public void addShow(Show show,
                        String theatreId) {

        Theatre theatre = theatres.get(theatreId);

        if (theatre == null) {
            return;
        }

        theatre.addShow(show);
        shows.put(show.getShowId(), show);
    }

    public Booking bookSeats(String userId,
                             String showId,
                             List<String> seatIds) {

        Show show = shows.get(showId);

        if (show == null) {
            return null;
        }

        if (!show.bookSeats(seatIds)) {
            return null;
        }

        List<Seat> bookedSeats = new ArrayList<>();

        Set<String> requiredSeats = new HashSet<>(seatIds);

        for (Seat seat : show.getScreen().getSeats()) {

            if (requiredSeats.contains(seat.getSeatId())) {
                bookedSeats.add(seat);
            }
        }

        String bookingId = "B" + bookingCounter++;

        Booking booking =
                new Booking(
                        bookingId,
                        userId,
                        show,
                        bookedSeats
                );

        bookings.put(bookingId, booking);

        return booking;
    }

    public boolean cancelBooking(String bookingId) {

        Booking booking = bookings.get(bookingId);

        if (booking == null) {
            return false;
        }

        List<String> seatIds = new ArrayList<>();

        for (Seat seat : booking.getSeats()) {
            seatIds.add(seat.getSeatId());
        }

        booking.getShow().cancelSeats(seatIds);

        bookings.remove(bookingId);

        return true;
    }

    public List<Seat> getAvailableSeats(String showId) {

        Show show = shows.get(showId);

        if (show == null) {
            return new ArrayList<>();
        }

        return show.getAvailableSeats();
    }

    public List<Show> getShows(String movieId,
                               String city) {

        List<Show> result = new ArrayList<>();

        for (Theatre theatre : theatres.values()) {

            if (!theatre.getCity().equals(city)) {
                continue;
            }

            for (Show show : theatre.getShows()) {

                if (show.getMovie()
                        .getMovieId()
                        .equals(movieId)) {

                    result.add(show);
                }
            }
        }

        return result;
    }
}

public class Main {

    public static void main(String[] args) {

        BookMyShow bms = new BookMyShow();

        Movie movie =
                new Movie(
                        "M1",
                        "Interstellar",
                        180
                );

        bms.addMovie(movie);

        List<Seat> seats = Arrays.asList(
                new Seat("A1"),
                new Seat("A2"),
                new Seat("A3"),
                new Seat("A4")
        );

        Screen screen =
                new Screen(
                        "SC1",
                        seats
                );

        Theatre theatre =
                new Theatre(
                        "T1",
                        "Hyderabad"
                );

        bms.addTheatre(theatre);

        Show show =
                new Show(
                        "S1",
                        movie,
                        screen,
                        "10:00 AM"
                );

        bms.addShow(show, "T1");

        Booking booking =
                bms.bookSeats(
                        "U1",
                        "S1",
                        Arrays.asList("A1", "A2")
                );

        if (booking != null) {
            System.out.println(
                    "Booking Successful : "
                            + booking.getBookingId()
            );
        }

        System.out.println(
                "Available Seats : "
                        + bms.getAvailableSeats("S1")
        );

        bms.cancelBooking(
                booking.getBookingId()
        );

        System.out.println(
                "After Cancellation : "
                        + bms.getAvailableSeats("S1")
        );
    }
}