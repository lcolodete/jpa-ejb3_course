<html> 
   <head>
      <title>Erro!</title>
   </head>

   <body>
      <font size='4' color='red'>
         O usuário <%= request.getRemoteUser() %> não possui o perfil <br/>
         necessário para acessar o URL requisitado.
         <br/>
      </font>
   </body>
</html>
