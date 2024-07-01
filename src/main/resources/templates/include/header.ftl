<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle!''}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .item {
            margin-bottom: 15px;
        }
        .header {
            background-color: #f8f9fa;
            padding: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .nav-links {
            list-style-type: none;
            padding: 0;
        }
        .nav-links li {
            display: inline;
            margin-right: 15px;
        }
        .logout-button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 5px 10px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="header">
    <ul class="nav-links">
        <li><a href="/">Home</a></li>
        <li><a href="/items/my">My items</a></li>
        <li><a href="/items/admin">Admin</a></li>
    </ul>
    <a class="logout-button" href="/logout">Log out</a>
</div>