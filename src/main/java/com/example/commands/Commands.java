package com.example.commands;

public interface Commands {
    String EXIT = "0";
    String ADD_AUTHOR = "1";
    String PRINT_ALL_AUTHORS = "2";
    String ADD_BOOK = "3";
    String DELETE_AUTHOR = "4";
    String DELETE_BOOK = "5";
    String UPDATE_AUTHOR = "6";
    String UPDATE_BOOK = "7";
    String ADD_CATEGORY = "8";
    String PRINT_ALL_CATEGORIES = "9";
    String PRINT_ALL_BOOKS = "10";

    static void printCommands(){
        System.out.println("Please input " + EXIT + " for EXIT");
        System.out.println("Please input " + ADD_AUTHOR + " for ADD_AUTHOR");
        System.out.println("Please input " + PRINT_ALL_AUTHORS + " for PRINT_ALL_AUTHORS");
        System.out.println("Please input " + ADD_BOOK + " for ADD_BOOK");
        System.out.println("Please input " + DELETE_AUTHOR + " for DELETE_AUTHOR");
        System.out.println("Please input " + DELETE_BOOK + " for DELETE_BOOK");
        System.out.println("Please input " + UPDATE_AUTHOR + " for UPDATE_AUTHOR");
        System.out.println("Please input " + UPDATE_BOOK + " for UPDATE_BOOK");
        System.out.println("Please input " + ADD_CATEGORY + " for ADD_CATEGORY");
        System.out.println("Please input " + PRINT_ALL_CATEGORIES + " for PRINT_ALL_CATEGORIES");
        System.out.println("Please input " + PRINT_ALL_BOOKS + " for PRINT_ALL_BOOKS");
    }

}
