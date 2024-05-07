/**
 * Curso: Elementos de Sistemas
 * Arquivo: CodeTest.java
 * Created by Lucas Hix <lucash@al.insper.edu.br>
 * Date: 21/11/2022
 */

package assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import org.junit.jupiter.api.Test;

public class CodeTest {
	
	/**
	 * Teste da conversão para binário
	 */
	
	@Test
	public void test_toBinary() {
		//assumeFalse(Code.toBinary("0") == null); // ignora test
		//assumeFalse(Code.toBinary("0").isEmpty()); // ignora test
		
		assertEquals("0000000000000000", Code.toBinary("0"));
		assertEquals("0000000000000001", Code.toBinary("1"));
		assertEquals("0000000000001010", Code.toBinary("10"));
		assertEquals("0000000001100100", Code.toBinary("100"));
		assertEquals("0000001111101000", Code.toBinary("1000"));
		assertEquals("0000111111111111", Code.toBinary("4095"));
		assertEquals("0101010101010101", Code.toBinary("21845"));
		assertEquals("0111111111111111", Code.toBinary("32767"));
		assertEquals("1111111111111111", Code.toBinary("65535"));
		//assertThrows(Exception.class, () -> Code.toBinary("65536"));
		//assertThrows(Exception.class, () -> Code.toBinary("-1"));
	}
	
	/**
	 * Teste da geração de código para Destino
	 */
	
	@Test
	public void testDestine() {
		//assumeFalse(Code.dest(new String[] { "nop" }) == null); // ignora test
		//assumeFalse(Code.dest(new String[] { "nop" }).isEmpty()); // ignora test
		
		assertEquals("0010", Code.dest(new String[] { "movw", "%A", "%D" }));
		assertEquals("0100", Code.dest(new String[] { "movw", "%A", "(%A)" }));
		assertEquals("0110", Code.dest(new String[] { "movw", "%A", "%D", "(%A)" }));
		assertEquals("0010", Code.dest(new String[] { "movw", "(%A)", "%D" }));
		assertEquals("0010", Code.dest(new String[] { "addw", "(%A)", "%D", "%D" }));
		assertEquals("0001", Code.dest(new String[] { "incw", "%A" }));
		assertEquals("0001", Code.dest(new String[] { "decw", "%A" }));
		assertEquals("0010", Code.dest(new String[] { "incw", "%D" }));
		assertEquals("0100", Code.dest(new String[] { "incw", "(%A)" }));
		assertEquals("0000", Code.dest(new String[] { "nop" }));
		assertEquals("0001", Code.dest(new String[] { "subw", "%D", "(%A)", "%A" }));
		assertEquals("0001", Code.dest(new String[] { "rsubw", "%D", "(%A)", "%A" }));
		assertEquals("0001", Code.dest(new String[] { "decw", "%A" }));
		assertEquals("0010", Code.dest(new String[] { "decw", "%D" }));
		assertEquals("0001", Code.dest(new String[] { "notw", "%A" }));
		assertEquals("0010", Code.dest(new String[] { "notw", "%D" }));
		assertEquals("0001", Code.dest(new String[] { "negw", "%A" }));
		assertEquals("0010", Code.dest(new String[] { "negw", "%D" }));
		assertEquals("0010", Code.dest(new String[] { "andw", "(%A)", "%D", "%D" }));
		assertEquals("0001", Code.dest(new String[] { "andw", "%D", "%A", "%A" }));
		assertEquals("0010", Code.dest(new String[] { "orw", "(%A)", "%D", "%D" }));
		assertEquals("0001", Code.dest(new String[] { "orw", "%D", "%A", "%A" }));
		assertEquals("0000", Code.dest(new String[] { "jmp" }));
		assertEquals("0000", Code.dest(new String[] { "je" }));
		assertEquals("0000", Code.dest(new String[] { "jne" }));
		assertEquals("0000", Code.dest(new String[] { "jg" }));
		assertEquals("0000", Code.dest(new String[] { "jge" }));
		assertEquals("0000", Code.dest(new String[] { "jl" }));
		assertEquals("0000", Code.dest(new String[] { "jle" }));
	}
	
	/**
	 * Teste da geração de código para Comando
	 */
	
	@Test
	public void testComputation() {
		//assumeFalse(Code.dest(new String[] { "nop" }) == null); // ignora test
		//assumeFalse(Code.dest(new String[] { "nop" }).isEmpty()); // ignora test
		
		assertEquals("000110000", Code.comp(new String[] { "movw", "%A", "%D" }));
		assertEquals("000001100", Code.comp(new String[] { "movw", "%D", "%A" }));
		assertEquals("000001100", Code.comp(new String[] { "movw", "%D", "(%A)" }));
		assertEquals("001110000", Code.comp(new String[] { "movw", "(%A)", "%A" }));
		assertEquals("000110000", Code.comp(new String[] { "movw", "%A", "(%A)" }));
		assertEquals("000000010", Code.comp(new String[] { "addw", "%A", "%D", "%D" }));
		assertEquals("001000010", Code.comp(new String[] { "addw", "(%A)", "%D", "%D" }));
		assertEquals("001110111", Code.comp(new String[] { "addw", "$1", "(%A)", "%D" }));
		assertEquals("000110111", Code.comp(new String[] { "incw", "%A" }));
		assertEquals("000011111", Code.comp(new String[] { "incw", "%D" }));
		assertEquals("001110111", Code.comp(new String[] { "incw", "(%A)" }));
		assertEquals("001110000", Code.comp(new String[] { "movw", "(%A)", "%D" }));
		assertEquals("001000010", Code.comp(new String[] { "addw", "(%A)", "%D", "%D" }));
		assertEquals("001010011", Code.comp(new String[] { "subw", "%D", "(%A)", "%A" }));
		assertEquals("001000111", Code.comp(new String[] { "rsubw", "%D", "(%A)", "%A" }));
		assertEquals("000110010", Code.comp(new String[] { "decw", "%A" }));
		assertEquals("000001110", Code.comp(new String[] { "decw", "%D" }));
		assertEquals("000110001", Code.comp(new String[] { "notw", "%A" }));
		assertEquals("000001101", Code.comp(new String[] { "notw", "%D" }));
		assertEquals("000110011", Code.comp(new String[] { "negw", "%A" }));
		assertEquals("000001111", Code.comp(new String[] { "negw", "%D" }));
		assertEquals("001000000", Code.comp(new String[] { "andw", "(%A)", "%D", "%D" }));
		assertEquals("000000000", Code.comp(new String[] { "andw", "%D", "%A", "%A" }));
		assertEquals("001010101", Code.comp(new String[] { "orw", "(%A)", "%D", "%D" }));
		assertEquals("000010101", Code.comp(new String[] { "orw", "%D", "%A", "%A" }));
		assertEquals("001110010", Code.comp(new String[] { "subw", "(%A)", "$1", "%A" }));
		assertEquals("000001100", Code.comp(new String[] { "jmp" }));
		assertEquals("000001100", Code.comp(new String[] { "je" }));
		assertEquals("000001100", Code.comp(new String[] { "jne" }));
		assertEquals("000001100", Code.comp(new String[] { "jg" }));
		assertEquals("000001100", Code.comp(new String[] { "jge" }));
		assertEquals("000001100", Code.comp(new String[] { "jl" }));
		assertEquals("000001100", Code.comp(new String[] { "jle" }));
	}
	
	/**
	 * Teste da geração de código para Jump
	 */
	
	@Test
	public void testJump() {
		//assumeFalse(Code.dest(new String[] { "nop" }) == null); // ignora test
		//assumeFalse(Code.dest(new String[] { "nop" }).isEmpty()); // ignora test
		
		assertEquals("000", Code.jump(new String[] { "movw", "%A", "%D" }));
		assertEquals("000", Code.jump(new String[] { "addw", "%A", "%D", "%D" }));
		assertEquals("000", Code.jump(new String[] { "movw", "%D", "%A" }));
		assertEquals("000", Code.jump(new String[] { "movw", "%D", "(%A)" }));
		assertEquals("000", Code.jump(new String[] { "incw", "%D" }));
		assertEquals("000", Code.jump(new String[] { "nop" }));
		assertEquals("000", Code.jump(new String[] { "movw", "(%A)", "%D" }));
		assertEquals("000", Code.jump(new String[] { "addw", "(%A)", "%D", "%D" }));
		assertEquals("000", Code.jump(new String[] { "subw", "%D", "(%A)", "%A" }));
		assertEquals("000", Code.jump(new String[] { "rsubw", "%D", "(%A)", "%A" }));
		assertEquals("000", Code.jump(new String[] { "decw", "%A" }));
		assertEquals("000", Code.jump(new String[] { "decw", "%D" }));
		assertEquals("000", Code.jump(new String[] { "notw", "%A" }));
		assertEquals("000", Code.jump(new String[] { "notw", "%D" }));
		assertEquals("000", Code.jump(new String[] { "negw", "%A" }));
		assertEquals("000", Code.jump(new String[] { "negw", "%D" }));
		assertEquals("000", Code.jump(new String[] { "andw", "(%A)", "%D", "%D" }));
		assertEquals("000", Code.jump(new String[] { "andw", "%D", "%A", "%A" }));
		assertEquals("000", Code.jump(new String[] { "orw", "(%A)", "%D", "%D" }));
		assertEquals("000", Code.jump(new String[] { "orw", "%D", "%A", "%A" }));
		assertEquals("111", Code.jump(new String[] { "jmp" }));
		assertEquals("010", Code.jump(new String[] { "je" }));
		assertEquals("101", Code.jump(new String[] { "jne" }));
		assertEquals("001", Code.jump(new String[] { "jg" }));
		assertEquals("011", Code.jump(new String[] { "jge" }));
		assertEquals("100", Code.jump(new String[] { "jl" }));
		assertEquals("110", Code.jump(new String[] { "jle" }));
	}
}
