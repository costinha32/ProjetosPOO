package teste;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.br.Calculadora;

class TesteCalculadora {
	
	Calculadora calc =  new Calculadora();
	
	@Test
	public void testSoma() {
		int resultado = calc.soma(5, 6);
		assertEquals(11, resultado);
	}
	@Test 
	public void testSubtrair() {
		int resultado =  calc.subtracao(30, 10);
		assertEquals(20, resultado);
	}
	@Test 
	public void tesDivisao() {
		int resultado =  calc.divisao(20, 5);
		assertEquals(4, resultado);
	}
	@Test 
	public void testMultiplicar() {
		int resultado =  calc.multiplicacao(5, 6);
		assertEquals(30, resultado);
	}

}