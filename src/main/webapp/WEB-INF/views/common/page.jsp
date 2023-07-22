<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.14.0/css/all.min.css"
            integrity="sha512-1PKOgIY59xJ8Co8+NE6FZ+LOAZKjy+KY8iq0G4B3CyeY6wYHN3yt9PW0XpSriVlkMXe40PTKnXrLnZ9+fkDaog=="
            crossorigin="anonymous"
    />
    <link rel="stylesheet" href="resources/css/style.css"/>
    <title>Mobile Tab Navigation</title>
</head>
<body>
<div class="phone">
    <img
            src="https://images.unsplash.com/photo-1480074568708-e7b720bb3f09?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1053&q=80"
            alt="home"
            class="content show"
    />
    <img
            src="https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80"
            alt="work"
            class="content"
    />
    <img
            src="https://images.unsplash.com/photo-1471107340929-a87cd0f5b5f3?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1266&q=80"
            alt="blog"
            class="content"
    />
    <img
            src="https://images.unsplash.com/photo-1522202176988-66273c2fd55f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80"
            alt="about"
            class="content"
    />
    <nav>
        <ul>
            <li class="active">
                <i class="fas fa-home"></i>
                <p>Home</p>
            </li>
            <li>
                <i class="fas fa-box"></i>
                <p>Work</p>
            </li>
            <li>
                <i class="fas fa-book-open"></i>
                <p>Blog</p>
            </li>
            <li>
                <i class="fas fa-users"></i>
                <p>About Us</p>
            </li>
        </ul>
    </nav>
</div>
<script src="script.js"></script>
</body>
</html>