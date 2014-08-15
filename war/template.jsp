<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN""http://www.w3.org/TR/html4/loose.dtd">
<html>

  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>BioGrapher</title>
    <meta name="description" content="A network generator for biological terms">
    <meta name="keywords" content="biographer, bioinformatics, network, rdf">
  
    <link rel="stylesheet" type="text/css" href="/css/common.css">
    <link rel="stylesheet" type="text/css" href="/css/font-awesome/css/font-awesome.css">
    <link href='//fonts.googleapis.com/css?family=Roboto+Slab' rel='stylesheet' type='text/css'>
    <!--[if IE 7]>
    <link rel="stylesheet" href="/css/font-awesome/css/font-awesome-ie7.min.css">
    <![endif]-->
   
    <script src="/js/jquery-1.9.1.js"></script>
    <script src="/js/jquery-ui.js"></script>
    <script src="/js/jquery.validate.js"></script>
    
  </head>
  
  <body>
   <div id='page_container'>
   
    <div id='page_header'>	
        <ul id='icon_container'>
<!--          <li><a class='icon' href='https://github.com/UMUC-Capstone-Project/IntelliLeaf-Resource-Enricher'>
              <i class="icon-github-sign"></i>&nbsp;Code</a></li> -->
          <li><a class='icon' href='mailto:kshefchek@gmail.com'><i class="icon-envelope-alt">
              </i>&nbsp;Contact</a></li>
        </ul>
    </div>
    
        <!--[if (gt IE 9)|!(IE)]><!--><div id='title_header'><div id='page_title'>BioGrapher</div></div>  <!--<![endif]-->    
        <!--[if (IE 7)|(IE 8)|(IE 9) ]><div id='page_IE'><div id='page_title'>BioGrapher</div></div> <![endif]-->
   
    
    <div id='main'>
    
      <div id='prim_nav_border'></div>
      
        <ul id='prim_nav_elements'>
          <li class='top'><a class='link' href='/'>Home</a></li>
          <li class='top'><a class='link' href='/examples'>Examples</a></li>
          <li class='top'><a class='link' href='/about'>Documentation&nbsp;<i class="icon-caret-down">
          </i></a>
             <ul class='hide'>
                <li><a href="/ontology.xml">RDF Ontology</a></li>
                <li><a href="https://github.com/UMUC-Capstone-Project/IntelliLeaf-Resource-Enricher">Github</a></li>
             </ul>
           </li>
           
        </ul>
    
     <div class='explain'><%= request.getAttribute("dcontent") %></div>
    </div>
   
   </div>
   
  </body>
  
</html>