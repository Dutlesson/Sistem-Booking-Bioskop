SISTEM BOOKING BIOSKOP

Aplikasi booking tiket bioskop berbasis console dengan implementasi Design Patterns.

Team Members

| Nama    | NIM       | Role     | Design Pattern         |
| Nazriel | 241511055 | Member 1 | Factory Method Pattern |
| Fathi   | 241511049 | Member 2 | Strategy Pattern       |
| Fiandra | 241511043 | Member 3 | Observer Pattern       |

Features

Admin Features:
- Manage Movies (CRUD)
- Manage Schedules (CRUD)
- Manage Food & Beverage (CRUD)
- View All Bookings
- Generate Sales Report

Customer Features:
- Browse Movies
- View Schedules with Dynamic Pricing
- Book Tickets (Multiple seat selection)
- Select Ticket Types (Regular/VIP/Student)
- Order Food & Beverage
- View Booking History

Design Patterns Implemented

1. Factory Method Pattern
Implemented by: Nazriel

Purpose: Create different types of tickets

Classes:
- `Ticket` (Abstract class)
- `RegularTicket` - Standard ticket (1.0x price)
- `VIPTicket` - Premium ticket (2.0x price)
- `StudentTicket` - Discounted ticket (0.75x price)
- `TicketFactory` - Factory for creating tickets

2. Strategy Pattern
Implemented by: Fathi

Purpose: Dynamic pricing based on day type

Classes:
- `PricingStrategy` (Interface)
- `WeekdayPricing` - Normal price (1.0x)
- `WeekendPricing` - Weekend markup (1.4x)
- `HolidayPricing` - Holiday markup (1.8x)

3. Observer Pattern
Implemented by:Fiandra

Purpose:Real-time seat availability updates

Classes:
- `SeatObserver` (Interface)
- `Seat` (Subject/Observable)
- `BookingObserver` - Track customer bookings
- `AdminObserver` - Monitor system availability

Data Storage

System menggunakan file-based storage (TXT files):

| File                | Content                           |
|---------------------|-----------------------------------|
| `users.txt`         | User accounts (admin & customers) |
| `movies.txt`        | Movie information                 |
| `schedules.txt`     | Show schedules with pricing       |
| `seats.txt`         | Seat layout and status            |
| `bookings.txt`      | Booking transactions              |
| `tickets.txt`       | Ticket details                    |
| `foodbeverages.txt` | F&B menu items                    |

Data Format:Pipe-delimited (|) format
Example: userId|username|password|name|role

Task Distribution

Nazriel (Member 1)
- Project setup & initialization
- File storage system (FileManager)
- User authentication
- Factory Method Pattern (Ticket system)
- Booking system integration

Fatih (Member 2)
- Strategy Pattern (Pricing system)
- Movie management
- Schedule management
- Food & Beverage system

Fiandra (Member 3)
- Observer Pattern (Seat monitoring)
- Console UI (Main application)
- Admin & Customer menus
- Pattern integration
- Testing & bug fixing
