<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN""http://www.w3.org/TR/html4/loose.dtd">
<html>

  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>BioGrapher</title>
    <meta name="description" content="A network generator for proteins, genes, and biological terms">
    
    <link rel="stylesheet" type="text/css" href="/css/common.css">
    <link rel="stylesheet" type="text/css" href="/css/font-awesome/css/font-awesome.css">
    <link href='//fonts.googleapis.com/css?family=Roboto+Slab' rel='stylesheet' type='text/css'>
    <!--[if IE 7]>
    <link rel="stylesheet" href="/css/font-awesome/css/font-awesome-ie7.min.css">
    <![endif]-->
   
    <script src="/js/jquery-1.9.1.js"></script>
    <script src="/js/jquery.validate.js"></script>
    <script src="/js/d3.v3.js" charset="utf-8"></script>
    <script src="/js/main.js"></script>
    
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
     <div class='explain'>BioGrapher- A network generator for proteins, genes, and other biological terms</div>
    </div>
    
    <div id='form_container'>
     <div id='input_form'>

     <form id = 'inputForm'>
      <fieldset>
       <legend>Please enter a protein, gene, or other biological term (required).
               An organism, species, or strain may be provided if applicable (optional).</legend>
       
       <table>
       <tr>
          <td class="label"><label for="term1">Term 1</label></td>
          <td class="field"><input id="term1" name="term1" type="text"/></td>
          <td class="status"></td>
       </tr>
       
       <tr>
          <td class="label"><label for="org1">Term 1 Organism</label></td>
          <td class="field"><input id="org1" name="org1" type="text"/></td>
          <td class="status"></td>
       </tr>
      
       
       <tr>
          <td class="label"><label for="term">Term 2</label></td>
          <td class="field"><input id="term2" name="term2" type="text"/></td>
          <td class="status"></td>
       </tr>
       
       <tr>
          <td class="label"><label for="org">Term 2 Organism</label></td>
          <td class="field"><input id="org2" name="org2" type="text"/></td>
          <td class="status"></td>
       </tr>
       
        <tr>
          <td class="label"></td>
          <td class="field"> <input id="submit" type="submit" value="Submit"/>
       </tr>
       
       </table>        
      </fieldset>
     </form>

     </div> 
    
     <div id='ajaxLoader'>
        <img src="/img/ajax-loader.gif">
         <span>
          &nbsp;Querying PubMed, UniProt, and Linked Life Data, this may take up to 30 seconds...
         </span>
     </div>
     
     <div id='graph_container'></div>
      
    </div>
   </div>
   
  <script>
  </script>
   
  </body>
  
</html>

