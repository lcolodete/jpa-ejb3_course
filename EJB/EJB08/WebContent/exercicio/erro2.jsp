<html> 
   <head>
      <title>Erro!</title>
   </head>

   <body>
      <font size='4' color='red'>
         O usu�rio <%= request.getRemoteUser() %> n�o possui o perfil <br/>
         necess�rio para acessar o URL requisitado.
         <br/>
      </font>
   </body>
</html>
