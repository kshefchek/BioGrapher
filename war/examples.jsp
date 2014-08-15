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
   
    <script src="/js/d3.v3.js"></script>
    <script src="/js/jquery-1.9.1.js"></script>
    
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
    
     <div class='explain'>Examples
       
     </div>
     <div id='example_container'>
        <span class='standard_text'>Example 1: BRCA1 (Homo sapiens), BARD1 (Homo sapiens)<br></span>
        <div id='example_graph'></div>
     </div>

    </div>
   
   </div>
   <script>
   
       
          d3.json("/json/BRCA1_BARD1.json", function(error, graph) {

	   	  var nodes = {};
	   	  var links = graph;
	   	  var pattern=/^http/; 
	   	  var linkCount = 0;
	   	  var width = 1100,
	      height = 900;
	   	  
	   	  var term1 = /^BARD1$/; 
	      var term2 = /^BRCA1$/; 
	   	  
		             
	   	  links.forEach(function(link) {
	   	    	  
	   	    	if (link.source.search(pattern)!=-1){
	   	    	    link.source = nodes[link.source] || (nodes[link.source] = {name: link.source, type: "uri"});
	   	    	}
	   	    	else{
	   	    		if (link.source.search(term1)!=-1){
	   	    			link.source = nodes[link.source] || (nodes[link.source] = {name: link.source, type: "literal",
	   	    			                                                           fixed: true, x: 15, y: 15});
	   	    		}
	   	    		else if (link.source.search(term2)!=-1){
	   	    			link.source = nodes[link.source] || (nodes[link.source] = {name: link.source, type: "literal",
                                   fixed: true, x: width-75, y: height-50});
	   	    		}
	   	    		
	   	    		else {
	   	    		link.source = nodes[link.source] || (nodes[link.source] = {name: link.source, type: "literal"});
	   	    		}
	   	    	}
	   	    	  
	   	    	if (link.target.search(pattern)!=-1){
	   	    	    link.target = nodes[link.target] || (nodes[link.target] = {name: link.target, type: "uri"});
	   	    	}
	   	    	else{
	   	    		link.target = nodes[link.target] || (nodes[link.target] = {name: link.target, type: "literal"});
	   	    	}
	   	  });

	   	  var color = d3.scale.category20();
	            
	   	  var force = d3.layout.force()
              .charge(-61000)
              .distance(0)
              .linkDistance(0)
              .gravity(1.4)
              .size([width, height]);    

	   	  var svg = d3.select("#example_graph").append("svg")
              .attr("width", width)
              .attr("height", height);          

	   
		  force
             .nodes(d3.values(nodes))
             .links(d3.values(links))
             .on("tick", tick)
             .start();

	   	             
		  svg.append("svg:defs").append("svg:marker")          
	   	     .attr("id", "arrow")
	   	     .attr("viewBox", "0 0 10 10")
	   	     .attr("refX", 24)
	   	     .attr("refY", 5)
	   	     .attr("markerUnits", "strokeWidth")
	   	     .attr("markerWidth", 8)
	   	     .attr("markerHeight", 6)
	   	     .attr("orient", "auto")
	   	     .append("svg:path")
	   	     .attr("d", "M 0 0 L 10 5 L 0 10 z");           
	   	              
		  var link = svg.selectAll(".gLink")
             .data(d3.values(links))
             .enter().append("g")
             .attr("class", "gLink")
             .append("line")
             .attr("class", "link")
             .style("stroke-width", 1)
             .attr("marker-end", "url(#arrow)")
             .attr("x1", function(d) { return d.source.x; })
             .attr("y1", function(d) { return d.source.y; })
             .attr("x2", function(d) { return d.target.x; })
             .attr("y2", function(d) { return d.target.y; });
	   	             
	   	                 
		  var linkText = svg.selectAll(".gLink")
              .data(d3.values(links))
              .append("text")
          
              .attr("x", function(d) {
                  if (d.target.x > d.source.x) { return (d.source.x + (d.target.x - d.source.x)/2); }
                  else { return (d.target.x + (d.source.x - d.target.x)/2); }
              })
               
              .attr("y", function(d) {
                  if (d.target.y > d.source.y) { return (d.source.y + (d.target.y - d.source.y)/2); }
                  else { return (d.target.y + (d.source.y - d.target.y)/2); }
              })
            
              .text(function(d) { return d.name; });
         	                   
		  var node_drag = d3.behavior.drag()
 	         .on("dragstart", dragstart)
 	         .on("drag", dragmove)
 	         .on("dragend", dragend);
	   	         
	   	  function dragstart(d, i) {
	   	     force.stop()
	   	  }

	   	  function dragmove(d, i) {
	   	     d.px += d3.event.dx;
	   	     d.py += d3.event.dy;
	   	     d.x += d3.event.dx;
	   	     d.y += d3.event.dy; 
	   	     tick();
	   	  }

	   	  function dragend(d, i) {
	   	     d.fixed = true;
	   	     tick();
	   	     force.start();
	   	  }
	   	                    
	   	  var node = svg.selectAll(".node")
             .data(d3.values(nodes))
             .enter().append("g")
             .attr("class", "node")
             .call(node_drag);
	   	  
	   	             
	   	 //add href to uri resources
             node.filter(function(d){return d.type == "uri"})
                 .append("svg:a")
                 .attr("xlink:href", function(d) { return d.name; })
                 .attr("class", "node")
                 .append("circle")
                 .attr("r", 8)
                 .style("fill", color(1));

             //add literal nodes
             node.filter(function(d){return d.type == "literal"})
                 .append("circle")
                 .attr("r", 8)
                 .style("fill", "#336666");
               
       
             node.append("title")
                 .text(function(d) { return d.name; });

             // add the text 
             node.append("text")
                 .attr("dx", 12)
                 .attr("dy", ".35em")
                 .text(function(d) { return d.name; });
             
            //Open a new tab/window when nodes are clicked   
             $(".node").click(function() {
        	  $(this).attr('target', '_blank');
        	 }); 

             function tick() {

              link
                  .attr("x1", function(d) { return d.source.x; })
                  .attr("y1", function(d) { return d.source.y; })
                  .attr("x2", function(d) { return d.target.x; })
                  .attr("y2", function(d) { return d.target.y; })
                  .attr("marker-end", "url(#arrow)");
              
              linkText
	              .attr("x", function(d) {
	                    if (d.target.x > d.source.x) {
	                    	return (d.source.x + (d.target.x - d.source.x)/2); 
	                    }
	                   else { 
	                	   return (d.target.x + (d.source.x - d.target.x)/2); 
	                    }
	              })
	              .attr("y", function(d) {
	                   if (d.target.y > d.source.y) { 
	                	   return ((d.source.y + (d.target.y - d.source.y)/2)+10);
	                   }
	                   else { 
	                	   return ((d.target.y + (d.source.y - d.target.y)/2)+10);
	                   }
	               });

              node
                  .attr("transform", function(d) {
            	   return "translate(" + d.x + "," + d.y + ")";
                   });           
	   	    }
           });
   </script>
   
  </body>
  
</html>