<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://stereotype.appify.be/tags" prefix="st"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Stereotype Demo</title>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
		<script type="text/javascript" src="js/bootstrap.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
		<style type="text/css">
			body {
				margin: 50px 0 0 0;
			}
		</style>
	</head>
	<body>
		<div class="navbar navbar-fixed-top">
			<div class="navbar-inner">
		    	<div class="container">
		    		<a class="brand" href="#">Stereotype Demo</a>
		    		<ul class="nav">
						<li class="active dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">Data <b class="caret"></b></a>
							<ul class="dropdown-menu">
								<c:forEach items="${beanModels}" var="beanModel">
									<li><a href="#"><st:message message="${beanModel.plural}" capitalize="true"/></a></li>
								</c:forEach>
							</ul>
						</li>
					</ul>
			    </div>
		  	</div>
		</div>
		<div class="container">
			<div class="row">
			</div>
		</div>
	</body>
</html>