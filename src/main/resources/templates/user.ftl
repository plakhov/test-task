<#import "/spring.ftl" as spring/>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>User Form</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
</head>
<body>
<h1>User Form</h1>
<div>
    <a href="/">Back to Index</a> |
    <a href="/logout">Logout</a>
</div>
<#if user?? >
    <form class="form-action" action="/users/add" method="post">
        First Name:<br>
        <input type="text" name="firstName" value="${user.firstName}">
        <br><br>
        Last Name:<br>
        <input type="text" name="lastName" value="${user.lastName}">
        <br><br>
        Login:<br>
        <input type="text" name="login" value="${user.login}">
        <br><br>
        Role:<br>
        <p><input type="radio" value="ROLE_ADMIN" <#if user.role == "ROLE_ADMIN"> checked </#if> name="role">Admin</p>
        <p><input type="radio" value="ROLE_USER" <#if user.role == "ROLE_USER"> checked </#if> name="role">User</p>
        <br><br>
        Password:<br>
        <input type="password" name="passwordHash" value="">
        <br><br>
        <input type="number" hidden name="id"  value="${user.id}">
        <br><br>
        <input type="submit" value="Submit">
    </form>
<#else>
    <form class="form-action" action="/users/add" method="post">
        First Name:<br>
        <input type="text" name="firstName">
        <br><br>
        Last Name:<br>
        <input type="text" name="lastName">
        <br><br>
        Login:<br>
        <input type="text" name="login">
        <br><br>
        Role:<br>
        <p><input type="radio" value="ROLE_ADMIN" name="role">Admin</p>
        <p><input type="radio" value="ROLE_USER" name="role">User</p>
        <br><br>
        Password:<br>
        <input type="password" name="passwordHash">
        <br><br>
        <input type="number" hidden name="id">
        <br><br>
        <input type="submit" value="Submit">
    </form>
</#if>
</body>
</html>