<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<meta charset="UTF-8">
	<title>WebApp Template</title>
	<meta name="viewport" content="width=device-width,initial-scale=1">
	<link rel="stylesheet" href="//fonts.googleapis.com/css?family=Lato:300,300italic,400,400italic,700,700italic,900">
	<link rel="stylesheet" href="<c:url value="/css/webapp.css"/>">
	<link rel="icon" type="image/x-icon" href="<c:url value="/img/favicon.ico"/>">
	<!--[if lt IE 9]>
	<script src="/js/html5shiv.js"></script>
	<script src="/js/respond.min.js"></script>
	<![endif]-->
</head>

<body class="wrap">
	<header>
		<nav class="mobile-nav show-on-mobiles">
			<ul>
				<li class="current"><a href="<c:url value="/home"/>">Home</a></li>
				<li class=""><a href="/docs">Doc<span class="show-on-mobiles">s</span><span class="hide-on-mobiles">umentation</span></a></li>
				<li><a href="<c:url value="/help"/>">Help</a></li>
				<li><a href="https://github.com/sdcote/boss"><span class="hide-on-mobiles">View on </span>GitHub</a></li>
			</ul>
		</nav>
		<div class="grid">
			<div class="unit one-third center-on-mobiles">
				<h1>
					<a href="<c:url value="/help"/>"> <span>WebTmpl</span> <img	src="<c:url value="/img/logo-2x.png"/>" width="249" height="115" alt="Logo"></a>
				</h1>
			</div>
			<nav class="main-nav unit two-thirds hide-on-mobiles">
				<ul>
					<li class="current"><a href="<c:url value="/home"/>">Home</a></li>
					<li class=""><a href="<c:url value="docs"/>">Doc<span class="show-on-mobiles">s</span><span class="hide-on-mobiles">umentation</span></a></li>
					<li><a href="<c:url value="/help"/>">Help</a></li>
					<li><a href="https://github.com/sdcote/boss"><span class="hide-on-mobiles">View on </span>GitHub</a></li>
				</ul>
			</nav>
		</div>
	</header>

	<section class="intro">
		<div class="grid">
			<div class="unit whole center-on-mobiles">
				<p class="first">Use this as a template for a complete web application.</p>
			</div>
		</div>
	</section>

	<section class="features">
		<div class="grid">
			<div class="unit one-third">
				<h2>Simple</h2>
				<p>Clone the project and start it running. Change what you need.</p>
				<a href="<c:url value="/docs/usage"/>">How this works...</a>
			</div>
			<div class="unit one-third">
				<h2>Static</h2>
				<p>Supports static resources as well as dynamic content.</p>
				<a href="<c:url value="/docs/templates"/>">Template guide...</a>
			</div>
			<div class="unit one-third">
				<h2>Practice-based</h2>
				<p>This uses the best practices for MVC.</p>
				<a href="<c:url value="/docs/mvc"/>">Model-View-Controller ?</a>
			</div>
			<div class="clear"></div>
		</div>
	</section>

	<section class="quickstart">
		<div class="grid">
			<div class="unit golden-small center-on-mobiles">
				<h4>Get up and running <em>in&nbsp;seconds</em>.</h4>
			</div>
			<div class="unit golden-large code">
				<p class="title">Quick-start Instructions</p>
				<div class="shell">
					<p class="line">
						<span class="path">~</span> <span class="prompt">$</span> <span
							class="command">gradlew jettyRun</span>
					</p>
					<p class="line"><span class="output"># =&gt; Now browse to http://localhost:8080<c:url value="/"/></span></p>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</section>

	<footer>
		<div class="grid">
			<div class="unit one-third center-on-mobiles">
				<p>The contents of this webapp are free for anyone to use and change.</p>
			</div>
			<div class="unit two-thirds align-right center-on-mobiles">
				<p>
					Proudly hosted by <a href="https://github.com/"> <img src="<c:url value="/img/footer-logo.png"/>" width="100" height="30" alt="GitHub Social coding"></a>
				</p>
			</div>
		</div>
	</footer>

</body>
</html>