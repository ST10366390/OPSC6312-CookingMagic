CookMagic - Recipe Management App

Overview
CookMagic is a comprehensive Android cooking application that allows users to browse, search, create, and manage recipes. The app features user authentication, recipe categorization, and a user-friendly interface for all cooking enthusiasts.

Features

User Authentication

• User registration and login

• Profile management with Firebase Firestore

• Secure logout functionality

Recipe Management
• Browse Recipes: View recipes in categorized tabs

• Search Functionality: Search recipes by title, ingredients, or description

• Create Recipes: Add new recipes with images, ingredients, and steps

• Edit Recipes: Modify existing recipes

• Delete Recipes: Remove unwanted recipes

• Recipe Details: View comprehensive recipe information

Category System
• Dynamic category management
• Add new categories
• Filter recipes by categories
• Pre-defined categories: All, Most Viewed, Most Downloaded

Advanced Features
• Real-time search with text filtering
• Image loading with Glide
• Full-screen image viewing
• Recipe sorting options
• User-specific recipe management

Technical Architecture

Frontend
Language: Kotlin
Architecture: MVVM (Model-View-ViewModel)
UI Components:

* Activities
* Fragments
* RecyclerViews with custom adapters
* ViewPager with tabs
* Custom filters

Backend Services
Authentication: Firebase Authentication
Database: Firebase Realtime Database & Firestore
Storage: Firebase Storage for images
Real-time Updates: Firebase listeners

Key Components

Activities
WelcomeActivity - Landing screen
LoginActivity - User authentication
ProfileActivity - User profile management
DashUserActivity - User dashboard with recipe categories
RecipeActivity - Recipe viewing and management
RecipeEditActivity - Recipe creation and editing
CategoryAddActivity - Category management
SearchActivity - Advanced recipe search

Fragments
RecipeUserFragment - Displays recipes by category

Adapters
AdapterCategory - Manages category list display
AdapterRecipeUser - Handles recipe list in user dashboard
SearchAdapter - Manages search results display

Filters
FilterCategory - Implements category search filtering
FilterRecipe - Implements recipe search filtering

Models
ModelUser - User data structure
ModelCategory - Category data structure
ModelRecipe - Recipe data structure

Database Structure

Firestore Collections
users - User profiles and preferences

Realtime Database Nodes
Categories - Recipe categories
Recipes - Recipe data with metadata

Recipe Data Model
{
"id": "unique_recipe_id",
"title": "Recipe Title",
"description": "Recipe description",
"ingredients": "List of ingredients",
"steps": "Cooking instructions",
"img": "Image URL",
"categoryId": "category_reference",
"timestamp": "creation_timestamp",
"uid": "user_id",
"cookingTime": "preparation_time",
"difficulty": "recipe_difficulty",
"servings": "number_of_servings"
}

Installation & Setup

Prerequisites
• Android Studio Arctic Fox or later
• Android SDK 21+
• Google Firebase account

Configuration

1. Clone the repository
2. Add your google-services.json file
3. Configure Firebase projects:

   * Authentication (Email/Password)
   * Realtime Database
   * Firestore Database
   * Storage (optional for images)

Build Instructions

1. Open project in Android Studio
2. Sync Gradle dependencies
3. Build and run on emulator or device

Usage Guide

For Users

1. Registration/Login: Create account or login
2. Browse Recipes: Navigate through categories
3. Search: Use search functionality to find specific recipes
4. View Recipe Details: Tap on any recipe for full details
5. Create Recipes: Use the add recipe functionality
6. Manage Profile: Update user information in profile section

For Developers
• The app follows Material Design guidelines
• Uses ViewBinding for type-safe view references
• Implements proper lifecycle management
• Includes error handling and user feedback

Dependencies

Main Libraries
• firebase-auth - User authentication
• firebase-database - Real-time data sync
• firebase-firestore - User data storage
• glide - Image loading and caching
• material-design - UI components

Development Tools
• AndroidX libraries
• Kotlin Coroutines
• ViewBinding

Contributing

1. Fork the repository
2. Create feature branch (git checkout -b feature/AmazingFeature)
3. Commit changes (git commit -m 'Add some AmazingFeature')
4. Push to branch (git push origin feature/AmazingFeature)
5. Open a Pull Request

License
This project is licensed under the MIT License - see the LICENSE file for details.

Support
For support and questions, contact the development team or create an issue in the repository.

Version
Current Version: 1.0.0

CookMagic - Bringing the joy of cooking to your fingertips!
