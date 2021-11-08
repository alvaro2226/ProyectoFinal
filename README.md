
# OrderTracker - Proyecto de D.A.M
## Hecho por Álvaro Morcillo Barbero

Este es el programa OrderTracker para sistemas Windows o Linux. Para descargar la aplicación de android ve a  
[OrderTracker para Android](https://github.com/alvaro2226/ProyectoFinal_Android).  Se utiliza principalmente para la consulta y modificación de los datos de esta base de datos: 

![enter image description here](https://programaloalvaro.es/archivos/modelobdd.png)

# Resumen de la aplicación

**Importante:** No es necesario instalar manualmente una base de datos local. Basta con tener la IP del servidor MySQL remoto o un servidor local. Se necesita su usuario y contraseña. El programa se encarga de crear las tablas por tanto no es necesario hacerlo. Si quieres instalar la base de datos tu mismo descargala de aquí:
https://programaloalvaro.es/archivos/modeloBDD.mwb

## Navegador de instalación
Cuando inicias por primera vez la aplicación, se abrirá un instalador que te ayudará a instalar el programa.
Para instalar el programa necesitas:
- La URL o IP del servidor de base de datos. Así como su usuario y contraseña.
- Crear un nombre de usuario y una contraseña para acceder al programa después.
- Datos de una empresa como por ejemplo: CIF, teléfono, dirección, etc.

Una vez conectado a la base de datos y creado lo anterior, se añadirán estos datos la misma base de datos y podremos acceder al programa con el usuario que hemos creado.
**Importante:** Si quieres volver a iniciar el instalador despues de la primera vez, tienes que eliminar el archivo `OrderTracker\resources\configuracion.properties`
	
## Ventana de inicio de sesión
Aqui simplemente se comprobará si el usuario existe en la base de datos y tiene permisos para acceder a ella. Si los tiene podrá acceder.

## Ventana del programa principal

Esto es por lo que estamos aquí. Desde aquí podremos acceder a la base de datos que se ha creado anteriormente (si el usuario tiene permisos) para poder modificar, añadir o eliminar sus datos.
### Apartado productos
Podremos añadir, modificar o eliminar productos existentes. También podemos consultar su stock y actualizar las imagenes y descripciones.
### Apartado pedidos
Desde aqui podemos consultar todos los pedidos activos. Datos como quien lo ha realizado o quien lo va a entregar, cuando y donde, que productos y cuantos, si se ha pagado o no, el método de pago, el precio total, etc.
### Apartado usuarios
Aqui podremos consultar los datos relacionados con todos los usuarios, tanto empleados como clientes. Aqui el rol del usuario que ha iniciado sesión juega un papel muy importante, puesto que si no tiene los permisos necesarios, no podrá crear otros usuarios (por ejemplo un empleado no puede crear un admin) o modificarlos.
# Características

Ahora una extensa lista con *casi* todo lo que hace la aplicación.




-   Si no existe la carpeta  `OrderTracker\resources`, la crea. 
 Aqui dentro crea una carpeta `OrderTracker\resources\logs`donde se guardaran los logs y un fichero `OrderTracker\resources\configuracion.properties` que sirve para guardar en el disco duro información importante sobre el programa. Si es la primera vez que iniciamos el programa, todo lo anterior se hará.
    
-   Crea un fichero `log_YY_MM_DD.txt` en la carpeta `OrderTracker\resources\logs` cada dia, donde transcribe lo que hace la aplicación junto a los errores que se puedan producir.
    
-   Si no existe el fichero `OrderTracker\resources\configuracion.properties` lo crea y añade algunos datos para que el programa funcione. Un ejemplo de estas propiedades es la URL de la base de datos.
    
-   Si es la **primera vez** que se inicia el programa, se abre un instalador compuesto por 5 pestañas
    
    -   Pide la URL del servidor de base de datos donde creará la base de datos, así como su usuario y contraseña. Tras comprobar que la conexión se establece correctamente, continúa a la siguiente pestaña.
        
    -   Para crear un usuario admin, el usuario necesita introducir un nombre y contraseña. Si ya tienes un usuario, aparece la opción para saltarse este paso. Comprueba todos los campos de texto antes de continuar.
        
    -   A continuación el usuario tendrá que introducir información sobre su empresa. Tambien comprueba los campos de texto, asi como que el CIF y el email sean válidos.
        
    -   Y los datos sobre la dirección física de la empresa. Igual que antes, comprueba todos los campos de texto.
        
    -   Ahora aparece un resumen con todos los datos introducidos por el usuario. Si quiere cambiar alguno, puede hacerlo. Si decide confirmar todos los datos. Se creará en la URL introducida, la base de datos del programa e inserta en ella los datos sobre la empresa y el usuario creados.
        

-   Si no es la primera vez que se inicia el programa o el instalador ha concluido correctamente. Se abrirá el frame para iniciar sesión necesario para acceder al programa.
    
-   Aqui se pide al usuario un usuario y contraseña (rol empleado o admin). Si el usuario existe en la base de datos y la contraseña es correcta, se cierra este frame y se abre el programa principal. Si es la primera vez que utilizamos el programa, aquí el usuario tiene que introducir el usuario admin y contraseña que ha introducido en el instalador.
    
-   El frame principal se compone de **3 partes** claramente diferenciadas. Una columna izquierda que hace de menú. En la parte superior aparece una tabla donde se mostrarán datos. En la parte inferior el usuario podrá insertar consultar y modificar los datos que se han seleccionado en la tabla superior, asi como añadir nuevos datos. Tambien aparece la tabla de las líneas del pedido, si se estan viendo los pedidos en la parte superior.
    
-   En la parte mas superior del programa aparece el nombre de usuario de quien ha iniciado sesión y su rol (admin o empleado) y un reloj.
    
-   Tambien aparece el logo del programa y el nombre del autor Álvaro Morcillo Barbero.
    
-   Si en el menú seleccionamos la opcion **Ver productos**:
    
    -   En la parte superior , en una tabla se muestra el nombre, descripcion,precio y stock de todos los productos que existen en la base de datos
        
    -   En la parte inferior se muestran campos de texto que tienen distintas funciones.
        
        -   Si se ha seleccionado un producto en la tabla superior, se muestran sus datos aquí
            
        -   Tras seleccionar un producto o escribir su nombre, podemos modificar sus datos aquí
            
        -   Podemos Aplicar cambios para modificar el producto o Añadir para crear uno nuevo.
            
    -   Tambien podemos borrar el producto seleccionado.
        
    -   Se muestra una imagen(si la tuviese) relacionada con el producto seleccionado. Podemos cambiarla.
        
    -   Para todo lo anterior, antes de confirmar la acción, aparece un dialog de confirmación que pregunta al usuario si está seguro.
        
-   Si en el menú seleccionamos la opcion **Ver pedidos**:
    
    -   En la parte superior, en una tabla se muestran datos sobre todos los pedidos que existen en la base de datos
        
    -   En la parte inferior, en una tabla se muestran datos sobre las líneas del pedido que seleccionemos en la tabla superior.
        
    -   También en la parte superior, podemos cancelar el pedido o generar una factura, tambien se muestran más datos sobre el pedido seleccionado.
        
-   Si en el menú seleccionamos la opcion **Ver usuarios**:
    
    -   En la parte superior, en una tabla se muestra datos sobre todos los usuarios que existen en la base de datos.
        
    -   En la parte inferior se muestran campos de texto que tienen distintas funciones.
        
        -   Si se ha seleccionado un usuario en la tabla superior, se muestran sus datos aquí
            
        -   Tras seleccionar un usuario o escribir su nombre, podemos modificar sus datos aquí
            
        -   Podemos Aplicar cambios para modificar el usuario.
            
    -   Todos los roles de usuario pueden crear otros usuarios a través del boton Añadir usuario
        
        -   Se abre un dialog que permite al usuario introducir los datos del usuario que se va a crear. Si todos los datos son correctos, tras un dialog de confirmacion, se añade el usuario a la base de datos
            
    -   Si el usuario seleccionado en la tabla superior es el mismo que ha iniciado sesión, aparecerá el boton de Cambiar contraseña.
        
        -   Este boton abre otro dialog que permite introducir la nueva contraseña.
            
    -   Tambien se habilitan los botones de Eliminar usuario y Modificar usuario
        
    -   Si el usuario seleccionado en la tabla superior tiene un rol superior al que ha iniciado sesión (ej: Admin > Empleado), este podrá eliminar dicho usuario o modificar sus datos
        
    -   Un empleado no puede modificar otro empleado o un rol superior.
        
    -   Un admin no puede modificar a otro admin a no ser que sea el mismo. Un admin puede modificar al resto de usuarios de rol inferior

