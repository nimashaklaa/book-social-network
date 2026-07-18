# Book Social Network

A full-stack book borrowing and social reading platform built with Spring Boot (backend) and Angular (frontend).

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot, Spring Security, JWT, Spring Data JPA, Thymeleaf |
| Frontend | Angular 17+, Bootstrap 5, FontAwesome |
| Database | PostgreSQL |
| API Docs | Swagger / OpenAPI |

---

## QA — Known Bugs & Issues

### Critical (Blocking)

| ID | File | Issue | Severity |
|---|---|---|---|
| BUG-001 | `BookMapper.java` | `toBorrowedBookResponse` references undefined variable `feed` and calls `.comment()` with no argument — **compile error, app will not start** | Critical |
| BUG-002 | `FeedbackService.java` | `getReviewForTransaction()` is implemented but no controller endpoint exposes it — completely unreachable via HTTP | Critical |
| BUG-003 | `BorrowedBookResponse.java` | `authorName` and `isbn` fields removed — frontend table still expects them, will render empty | Critical |

### High (Functionally Broken)

| ID | File | Issue | Severity |
|---|---|---|---|
| BUG-004 | `FeedbackService.java` | `save()` does not verify the user actually borrowed the book — any authenticated user can leave feedback on any available book without borrowing it | High |
| BUG-005 | `BookMapper.java` | `toBook()` passes `request.id()` into entity on create — a client can POST with an existing ID and silently overwrite another book | High |
| BUG-006 | `GlobalExceptionHandler.java` | `EntityNotFoundException` and `IllegalArgumentException` are not handled — both fall through to generic 500 handler instead of returning 404/400 | High |
| BUG-007 | `AuthenticationService.java` | `RuntimeException("Token not found")` and `RuntimeException("Token has expired")` are not caught by a specific handler — return 500 instead of 400 | High |
| BUG-008 | `TokenRepository.java` | Old activation tokens are never invalidated when a new one is issued on resend — token table grows indefinitely | High |
| BUG-009 | `JwtFilter.java` | `sendError()` commits the HTTP response before `setContentType()` and `write()` — the custom JSON error body for expired JWT is never actually delivered to the client | High |

### Medium

| ID | File | Issue | Severity |
|---|---|---|---|
| BUG-010 | `BookService.java` | N+1 query in `findAllBooks()` — 2 extra DB queries fired per book per page (isAlreadyBorrowed + isCurrentlyBorrowed) | Medium |
| BUG-011 | `BookRepository.java` | `findByTitle()` returns `Book` not `Optional<Book>` — crashes silently on null or if multiple books share a title | Medium |
| BUG-012 | `Token.java` | `token` field has no unique database constraint — duplicate OTP codes are structurally possible | Medium |
| BUG-013 | `BeansConfig.java` | CORS origin hardcoded to `http://localhost:4200` — no environment-based override for staging or production | Medium |
| BUG-014 | `User.java` | `dateOfBirth` field exists on entity but `RegistrationRequest` has no such field — always null after registration | Medium |
| BUG-015 | `JwtFilter.java` | Non-expiry JWT exceptions (malformed token, bad signature) are not caught — produce unhandled 500 instead of 401 | Medium |

### Low / Code Quality

| ID | File | Issue | Severity |
|---|---|---|---|
| BUG-016 | `JwtService.java` | Single-arg `generateToken(UserDetails)` overload is never called anywhere — dead code | Low |
| BUG-017 | `FeedbackRepository.java` | Extends `JpaSpecificationExecutor` but Specifications are never used in any service | Low |
| BUG-018 | `BusinessErrorCodes.java` | `INCORRECT_PASSWORD` and `NEW_PASSWORD_DOES_NOT_MATCH` defined but no password change feature exists | Low |
| BUG-019 | `SecurityConfig.java` | `@EnableMethodSecurity(securedEnabled = true)` is active but no controller uses `@Secured` / `@PreAuthorize` | Low |
| BUG-020 | `User.java` | `@ManyToMany` on `roles` has no `@JoinTable` — Hibernate auto-names the join table, fragile on refactor | Low |
| BUG-021 | `BookController.java` | `findAllBooksByBooks()` method name is a typo — should be `findAllBooksByOwner()` | Low |
| BUG-022 | `AuthController.java` | `GET /auth/activate-account` returns `void` — client receives 200 with empty body, no success message | Low |
| BUG-023 | `FeedbackMapper.java` | `toFeedback()` creates a detached `Book` entity reference instead of passing the already-loaded instance from the service | Low |

---

## PM — Incoming Features & UI Roadmap

### In Progress

#### My Returned Books Page (Backend + Frontend)
**Goal:** Book owners can see who returned their books, when, and what feedback they left.

Backend remaining:
- [ ] Fix `BookMapper.toBorrowedBookResponse` — inject `FeedbackRepository`, call `findBySpecificTransactionFeedback`, map `rate` (borrower's actual rating) and `comment`
- [ ] Expose `getReviewForTransaction` via a controller endpoint

Frontend remaining:
- [ ] Wire up `loadData()` — call `GET /books/returned`
- [ ] Wire up `approveReturn()` — call `PATCH /books/approve/{book-id}`, reload on success
- [ ] Wire up `openDetails()` and `closeDrawer()` drawer signals
- [ ] Re-sync OpenAPI after backend changes to get updated `BorrowedBookResponse` model

---

### Planned Features

#### My Borrowed Books Page
Currently a stub (`my-borrowed-books works!`). Full implementation needed.

- [ ] Table: Title, Author, ISBN, Borrowed Date, Return Status, Actions
- [ ] Action: Return Book button — calls `POST /books/borrow/return/{book-id}`
- [ ] Action: Leave Feedback button — opens drawer with rating (1–5 stars) + comment form
- [ ] Feedback form links `historyId` so feedback is tied to the specific transaction
- [ ] After returning: show "Return Pending Approval" status badge

#### My Waiting List Page
Currently a stub. Define scope — possible features:
- [ ] Books the user has wishlisted / wants to borrow next
- [ ] Notification when a book becomes available

#### Book Details Page / Drawer
- [ ] "Details" button on BookCard has no handler — wire it up
- [ ] Show full synopsis, owner name, average rating, all community feedback
- [ ] Star breakdown chart (how many 5★, 4★, etc.)

#### Wishlist / Heart Button
- [ ] Heart button on BookCard has no handler
- [ ] Backend: wishlist entity or flag needed
- [ ] Frontend: toggle saved state, show saved books in "My Waiting List"

#### User Profile
- [ ] Registration collects `dateOfBirth` but field is never saved — add to `RegistrationRequest`
- [ ] Profile page: display name, email, date of birth, books owned, books borrowed count
- [ ] Avatar / profile picture upload

#### Search
- [ ] Search input in navbar is UI-only — no handler wired
- [ ] Backend: full-text search on title, author, ISBN
- [ ] Frontend: real-time search with debounce, results dropdown or navigate to filtered book list

#### Notifications
- [ ] Notify owner when someone borrows their book
- [ ] Notify owner when borrower returns a book (pending approval)
- [ ] Notify borrower when owner approves the return
- [ ] Notify borrower when a wishlisted book becomes available

#### Password Management
- [ ] `BusinessErrorCodes` already has `INCORRECT_PASSWORD` and `NEW_PASSWORD_DOES_NOT_MATCH` — the feature was planned but never built
- [ ] Change password flow: current password + new password + confirm
- [ ] Forgot password flow via email

#### Admin Panel
- [ ] View all users
- [ ] Suspend / unlock accounts
- [ ] View all books and transactions
- [ ] Role management

---

### UI Improvements

| Area | Idea |
|---|---|
| Book Card | Show "Borrowed" overlay badge when `borrowedByCurrentUser = true` |
| Book Card | Show "Unavailable" dimmed overlay when `borrowedByAnotherUser = true` |
| My Books Table | Add total borrow count column per book |
| My Returned Books | Show number of days the book was borrowed (borrowedDate → returnedDate) |
| My Borrowed Books | Show countdown / days since borrowed |
| All Pages | Toast notifications for success/error actions instead of just error banners |
| Login / Register | Add loading spinner on submit button |
| Navbar | Show unread notification badge count |
| Navbar | Show logged-in user's name dynamically (currently hardcoded "Welcome amandi") |
| Pagination | Show total record count e.g. "Showing 1–10 of 43 books" |

---

## API Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/auth/register` | Register new user |
| POST | `/auth/authenticate` | Login, returns JWT |
| GET | `/auth/activate-account?token=` | Activate account via OTP |
| POST | `/books` | Create a book |
| GET | `/books` | List all displayable books (paginated) |
| GET | `/books/{book-id}` | Get book by ID |
| PATCH | `/books/update/{book-id}` | Update book details |
| GET | `/books/owner` | My books |
| GET | `/books/borrowed` | Books I have borrowed |
| GET | `/books/returned` | Books returned to me (as owner) |
| PATCH | `/books/shareable/{book-id}` | Toggle shareable status |
| PATCH | `/books/archived/{book-id}` | Toggle archived status |
| POST | `/books/borrow/{book-id}` | Borrow a book |
| POST | `/books/borrow/return/{book-id}` | Return a borrowed book |
| PATCH | `/books/approve/{book-id}` | Approve a return (owner) |
| POST | `/books/cover/{book-id}` | Upload book cover image |
| POST | `/feedback` | Submit feedback for a book |
| GET | `/feedback/book/{book-id}` | Get all feedback for a book |