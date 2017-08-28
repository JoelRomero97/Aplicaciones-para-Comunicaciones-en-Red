import java.io.Serializable;
public class X implements Serializable				//La clase debe implementar a serializable siempre para enviar objetos
{
	String nombre, apellido;
	long boleta;
	//transient int edad;							//Si ponemos el modificador 'transient' no se serializa 
	int edad;
	public X (String n, String a, int e, long b)
	{
		this.nombre = n;
		this.apellido = a;
		this.edad = e;
		this.boleta = b;
	}

	String getNombre ()
	{
		return this.nombre;
	}

	String getApellido ()
	{
		return this.apellido;
	}

	long getBoleta ()
	{
		return this.boleta;
	}

	int getEdad ()
	{
		return this.edad;
	}
}