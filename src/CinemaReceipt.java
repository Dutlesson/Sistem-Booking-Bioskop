public class CinemaReceipt {

    public static void main(String[] args) {
        printReceipt();
    }

    public static void printReceipt() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║          CINEPLEX MOVIE THEATER                ║");
        System.out.println("║        Jl. Sudirman No. 123, Jakarta           ║");
        System.out.println("║           Telp: (021) 1234-5678                ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║              BOOKING RECEIPT                   ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("  Booking ID       : BK-2024-001                 ");
        System.out.println("  Date             : 20 November 2024            ");
        System.out.println("  Time             : 14:30 WIB                   ");
        System.out.println("                                                  ");
        System.out.println("  Customer Name    : John Doe                    ");
        System.out.println("  Email            : john.doe@email.com          ");
        System.out.println("  Phone            : 08123456789                 ");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("  MOVIE DETAILS                                  ");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("  Title            : SPIDER-MAN: NO WAY HOME     ");
        System.out.println("  Genre            : Action, Adventure           ");
        System.out.println("  Duration         : 148 minutes                 ");
        System.out.println("  Rating           : PG-13                       ");
        System.out.println("                                                  ");
        System.out.println("  Studio           : Studio 2 (IMAX)             ");
        System.out.println("  Showtime         : 19:30 - 22:00              ");
        System.out.println("  Seats            : A5, A6                      ");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("  TICKET DETAILS                                 ");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("  2x VIP Ticket (Weekend)      Rp 210.000       ");
        System.out.println("                                                  ");
        System.out.println("  FOOD & BEVERAGE                                ");
        System.out.println("  1x Large Popcorn             Rp  45.000       ");
        System.out.println("  2x Coca Cola                 Rp  30.000       ");
        System.out.println("  1x Nachos Combo              Rp  55.000       ");
        System.out.println("                               ─────────────    ");
        System.out.println("  Subtotal                     Rp 340.000       ");
        System.out.println("  Admin Fee (5%)               Rp  17.000       ");
        System.out.println("  ════════════════════════════════════════════  ");
        System.out.println("  TOTAL PAYMENT                Rp 357.000       ");
        System.out.println("  ════════════════════════════════════════════  ");
        System.out.println("                                                  ");
        System.out.println("  Payment Method   : Debit Card                  ");
        System.out.println("  Payment Status   : PAID ✓                     ");
        System.out.println("                                                  ");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("         IMPORTANT INFORMATIONS                  ");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("  • Please arrive 15 minutes before showtime     ");
        System.out.println("  • Ticket cannot be refunded or exchanged       ");
        System.out.println("  • Keep this receipt for entry verification     ");
        System.out.println("  • Enjoy your movie! 🎬🍿                      ");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("         Thank you for choosing CINEPLEX!        ");
        System.out.println("            www.cineplex-theater.com             ");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Generated at: " + java.time.LocalDateTime.now());
    }
}