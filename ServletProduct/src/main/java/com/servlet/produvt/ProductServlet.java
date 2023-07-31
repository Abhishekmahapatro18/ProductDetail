package com.servlet.produvt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Database connection settings (Replace these with your own database credentials)
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/product";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Abhishek@1803";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		// Get the product ID from the request parameter
        String productIdStr = request.getParameter("product_id");

        // Check if the product ID is provided and is a valid integer
        if (productIdStr != null && productIdStr.matches("\\d+")) {
            int productId = Integer.parseInt(productIdStr);
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish a database connection
                try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                    // Prepare the SQL query to retrieve product details by ID
                    String sql = "SELECT * FROM product WHERE Id = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, productId);
                        // Execute the query
                        try (ResultSet resultSet = statement.executeQuery()) {
                            // Check if a product with the given ID exists
                            if (resultSet.next()) {
                                // Product details found, retrieve and display them
                                int Id = resultSet.getInt("Id");
                                String ProductName = resultSet.getString("ProductName");
                                double Price = resultSet.getDouble("Price");
                                double Weight = resultSet.getDouble("Weight");

                                // Generate HTML response to display product details
                                response.setContentType("text/html");
                                PrintWriter out = response.getWriter();
                                out.println("<html><body>");
                                out.println("<h1>Product Details</h1>");
                                out.println("<p><b>Product ID:</b> " + Id + "</p>");
                                out.println("<p><b>Product Name:</b> " + ProductName + "</p>");
                                out.println("<p><b>Price:</b> $" + Price + "</p>");
                                out.println("<p><b>Weight:</b> " + Weight + " lbs</p>");
                                out.println("</body></html>");
                            } else {
                                // Product not found
                                showErrorPage(response, "Product not found.");
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException | SQLException e) {
                // Database connection error or other exception occurred
                showErrorPage(response, "An error occurred while processing your request.");
            }
        } else {
            // Invalid product ID provided
            showErrorPage(response, "Invalid product ID.");
        }
	}
	private void showErrorPage(HttpServletResponse response, String errorMessage)
            throws IOException {
        // Generate HTML response to display error message
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Error</h1>");
        out.println("<p>" + errorMessage + "</p>");
        out.println("</body></html>");
    }
	
	}

