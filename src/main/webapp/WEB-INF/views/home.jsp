<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%> <%@ taglib prefix="mvcqs" uri="/WEB-INF/mvcqs.tld"%><!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="">
	<meta name="author" content="">
	<title>${title}</title>
	<link rel="icon" type="image/x-icon" href="<c:url value="/img/favicon.ico"/>">
	<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
	<link href="<c:url value="/css/plugins/metisMenu/metisMenu.min.css"/>" rel="stylesheet">
	<link href="<c:url value="/css/plugins/timeline.css"/>" rel="stylesheet">
	<link href="<c:url value="/css/sb-admin-2.css"/>" rel="stylesheet">
	<link href="<c:url value="/css/plugins/morris.css"/>" rel="stylesheet">
	<link href="<c:url value="/font-awesome-4.1.0/css/font-awesome.min.css"/>" rel="stylesheet" type="text/css">

	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->
</head>
<body>
	<div id="wrapper">
		<!-- Navigation -->
		<mvcqs:MainNav />

		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header">Home</h1>
						<p>This page is the starting point for the webapp.</p>
						<p>If the session is logged-in, then the navigation menu will show what options are available to the currently logged-in user. Anonymous session (those not logged-in) will see the basic home page.</p>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.container-fluid -->
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	<script src="<c:url value="/js/jquery.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/js/plugins/metisMenu/metisMenu.min.js"/>"></script>
	<script src="<c:url value="/js/sb-admin-2.js"/>"></script>
</body>
</html>