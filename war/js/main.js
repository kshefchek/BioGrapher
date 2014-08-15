$(document).ready(function(){
	   $("#inputForm").validate({
		 rules: {
			term1: {
				required: true,
			},
			term2:{
				required: true
			}
		 },
		 messages:{
			term1: " Please enter an input term",
			term2: " Please enter an input term"
		 },
		 errorPlacement: function (error, element){
			error.appendTo(element.parent().next());
		 }
	   });
});

$(document).ready(function(){
    $('#inputForm').submit(function(){
    	
    	var t1Value = $('input:text[id=term1]').val();
    	var t2Value = $('input:text[id=term2]').val();
    	
    	if ((t1Value.length > 0) && (t2Value.length > 0) ){
    		
    		$('#inputForm').hide();
    		
    		
    		$.ajax({
   	         type: 'GET',
   	         dataType: 'json',
   	         url: 'biographer',
   	         data: $('#inputForm').serialize(),
   	         success: function(graph){

   	             var nodes = {};
   	             var links = graph;
   	             var pattern=/^http/; 
   	             var linkCount = 0;
   	             var width = 1000,
                     height = 800;
	             
   	             links.forEach(function(link) {
   	            	 linkCount++;
   	    	  
   	    	         if (link.source.search(pattern)!=-1){
   	    	             link.source = nodes[link.source] || (nodes[link.source] = {name: link.source, type: "uri"});
   	    	         }
   	    	         else{
  	   	    		     if (link.source.search(t1Value)!=-1){
  	   	    			     link.source = nodes[link.source] || (nodes[link.source] = {name: link.source, type: "literal",
  	   	    			                                                                fixed: true, x: 15, y: 15});
  	   	    		     }
  	   	    		    else if (link.source.search(t2Value)!=-1){
  	   	    			    link.source = nodes[link.source] || (nodes[link.source] = {name: link.source, type: "literal",
                                                                                 fixed: true, x: width-100, y: height-40});
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
   	             
   	             
   	             
   	             if (linkCount == 0){
   	            	$('#ajaxLoader').html(
   	            			"<span>No results found!<br>" +
   	            			"BioGrapher could not establish a relationship between " +
   	            			t1Value + " and " + t2Value + "<br><br>"  +
   	            			"<a class='results' href='/'>Click here</a> to reload form</span>"
   	            			)
   	            	 height = 500;
  	                 width = 500;
   	             }
   	             else {$('#ajaxLoader').hide();}
   	            
   	             
   	             if (linkCount > 20){
   	                 height = 1000;
   	                 width = 1200;
   	             }


   	             var color = d3.scale.category20();
            
   	             var force = d3.layout.force()
   	                 .charge(-60000)
                     .distance(0)
                     .linkDistance(0)
                     .gravity(2.5)
   	                 .size([width, height]);

   	             var svg = d3.select("#graph_container").append("svg")
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
   	                force.stop() // stops the force auto positioning before you start dragging
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
   		                	   return ((d.source.y + (d.target.y - d.source.y)/2));
   		                   }
   		                   else { 
   		                	   return ((d.target.y + (d.source.y - d.target.y)/2));
   		                   }
   		               });

   	              node
   	                  .attr("transform", function(d) {
   	            	   return "translate(" + d.x + "," + d.y + ")";
   	                   });           	              
   	             }
   	         }
   	        });	
        }
    	else{
    	    return false;
    	}
	  return false;	
    });
});
