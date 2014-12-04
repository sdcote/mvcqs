<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Login</title>

    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/jquery-ui.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/plugins/metisMenu/metisMenu.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/sb-admin-2.css"/>" rel="stylesheet">
    <link href="<c:url value="/font-awesome-4.1.0/css/font-awesome.min.css"/>" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Please Sign In</h3>
                    </div>
                    <div class="panel-body">
                        <form id="loginForm" action="<c:url value="/do/login"/>" method="post">
                        
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="E-mail" name="login" type="email" autofocus>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Password" name="passwd" type="password" value="">
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input name="remember" type="checkbox" value="true">Remember Me
                                    </label>
                                </div>
                                <button type="button" class="btn btn-primary" id="loginLink">Log In</button>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="<c:url value="/js/jquery.js"/>"></script>
    <script src="<c:url value="/js/jquery-ui.js"/>"></script>
    <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
    <script src="<c:url value="/js/plugins/metisMenu/metisMenu.min.js"/>"></script>
    <script src="<c:url value="/js/sb-admin-2.js"/>"></script>
    <script src="<c:url value="/js/Logger.js"/>"></script>

	<script src="<c:url value="/js/Util.js"/>" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
			Util = new UtilImpl('/mvcqs', '/mvcqs/static');
		});
	</script>
    <script src="<c:url value="/js/Login.js"/>" type="text/javascript"></script>
    

</body>

</html>
