# Banking Management System
A modern, responsive web application for managing banking operations with a user-friendly interface and secure transaction handling.

# 🌟 Features
-  User Authentication: Secure registration and login system

- Account Management: Create and manage multiple bank accounts (Savings/Credit)

- Transaction Operations:

 Deposit funds

 Withdraw funds

 Transfer between accounts

- Real-time Updates: Instant balance updates after transactions

- Responsive Design: Works seamlessly on desktop and mobile devices

- Interactive UI: Modern design with smooth animations and visual feedback
- 
  # 🛠️ Technologies Used
- Frontend: HTML5, CSS3, JavaScript (ES6+)

- Styling: Custom CSS with CSS variables and Flexbox/Grid

- Icons: Font Awesome 6.4.0

- Fonts: Google Fonts (Inter)

- Backend API: RESTful API (Java Spring Boot)
  
  # Prerequisites
Before running this application, ensure you have:

1. Backend Server: The Java Spring Boot API server must be running

- Server URL: http://localhost:8080

- Required endpoints: /api/auth/register, /api/auth/login, /api/accounts/*

2. Web Browser: Modern browser with JavaScript support

- Chrome 90+

- Firefox 88+

- Safari 14+

- Edge 90+

  # 🚀 Installation & Setup
Clone or Download the Project

bash
# If using git
git clone <repository-url>
cd banking-management-system
Start the Backend Server

- Ensure the Java Spring Boot application is running on port 8080

- The API endpoints should be accessible at http://localhost:8080/api/

- Open the Application

- Open index.html in a web browser

Or serve it using a local web server:

bash
# Using Python
python -m http.server 8000

# Using Node.js
npx http-server
Access the Application

Navigate to http://localhost:8000 (or the port you specified)

# 📱 How to Use
Registration
Click on "Register" on the login page

- Fill in username, email, and password

- Submit the form to create your account

- After successful registration, switch to the login form

Login
Enter your username and password

Click "Login" to access your dashboard

# Dashboard Features
- View Accounts: See all your accounts with balances

- Create Account: Add new Savings or Credit accounts

- Deposit Funds: Add money to any account

- Withdraw Funds: Remove money from accounts

- Transfer Funds: Move money between your accounts

# 🎨 UI Components
Authentication Section
- Toggle between Login and Register forms

- Form validation and error handling

- Success/error messages with visual feedback

# Dashboard

- Sidebar Navigation: Quick access to all features

- Profile Section: Display user information

- Account Management: View and manage all accounts

- Transaction Forms: Clean, intuitive forms for banking operations

# Visual Design
- Modern color scheme with banking-themed colors

- Smooth animations and hover effects

- Responsive layout for all screen sizes

- Interactive mouse trail effect

# 🔧 API Endpoints Used
The frontend communicates with these backend endpoints:

- POST /api/auth/register - User registration

- POST /api/auth/login - User authentication

- POST /api/accounts/create - Create new account

- POST /api/accounts/deposit - Deposit funds

- POST /api/accounts/withdraw - Withdraw funds

- POST /api/accounts/transfer - Transfer between accounts

# 🐛 Troubleshooting
Common Issues
- "Network Error. Check if server is running."

- Ensure the Java backend is running on port 8080

- Check console for CORS errors

- Forms not submitting

- Verify all required fields are filled

- Check browser console for JavaScript errors

- Styling issues

- Ensure all CSS and font resources load properly

- Check browser compatibility

# Browser Support
This application supports modern browsers with:

- ES6+ JavaScript support

- CSS Grid and Flexbox support

- Fetch API support

# 📞 Support
For issues related to:

- Frontend functionality: Check browser console for errors

- Backend connectivity: Ensure API server is running

- Transaction errors: Verify account numbers and sufficient funds

# 📄 License
This project is created for educational/demonstration purposes.
