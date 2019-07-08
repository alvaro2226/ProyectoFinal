# ProyectoFinal del curso DAM - Álvaro M.B

Errores detectados sin corregir:
  - Se tiene que modificar la clase operacionesBDD, no deben hacer throw los m�todos,
     ya que si no, el commit se hace igualmente.
  - El fichero de consiguracion se sobreescribe cada vez que se intenta a�adir una propiedad nueva.
     No es capaz de detectar si es la primera vez que se inicia el programa, porque la propiedad
     se sobreescribe.
  - La URL de la base de datos en configuracion.properties es localhost (debería ser la IP del servidor).
  - Las carpetas dist/build no hacen commit/push a GitHub. Por tanto no se suben las librerías externas.
  - <del>En el componente botón, la ruta a las imágenes es absoluta (debería ser relativa al proyecto). Por tanto, no 
     se puede visualizar el botón. </del> Corregido, he eliminado estos botones y he utilizado las librerias de rojeru san
  - <del>Ajustar el layout del frame de bienvenida porque los componentes se mueven a sus anchas dependiendo del SO. </del>
      Corregido usando un Absolute Layout.
  - <del>El dialogo de confirmacion y el frame de bienvenida no comparten bien los datos.</del>

Funcionalidades aun sin implementar en orden de prioridad:
  - <del>Añadir el campo teléfono a datos_empresa </del>
  - <del>Crear el script de la base de datos. </del> No hay script, los comandos se ejecutan desde código
  - <del>Si al añadir el servidor de bdd, la bdd no existe, hay que crearla mediante el script anterior. </del>
  - Si las 2 funcionales anteriores funcionan, se debe omitir la creación del admin/datos_empresa
  - <del>Almacenar imágenes en un servidor (mirar 000host) o en su defecto almacenarlas en la bdd en forma de blobs.</del> En forma de          blobs temporalmente.
