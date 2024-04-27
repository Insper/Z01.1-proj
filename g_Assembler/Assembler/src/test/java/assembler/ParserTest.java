/**
 * Curso: Elementos de Sistemas
 * Arquivo: CodeTest.java
 * Created by Lucas Hix <lucash@al.insper.edu.br>
 * Date: 21/11/2022
 */

package assembler;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ParserTest {
    
    private static Parser parser;
    
    @BeforeAll
    public static void setUp() throws IOException {
        parser = new Parser("src/test/resources/testEmpty.nasm");
    }
    
    /**
     * Testa a correta indentificação do tipo de comando
     */
    
    @Test
    public void testCommandType() {
        assumeFalse(parser.commandType("nop") == null);
        
        assertEquals(Parser.CommandType.A_COMMAND, parser.commandType("leaw $0,%A"));
        assertEquals(Parser.CommandType.L_COMMAND, parser.commandType("abc:"));
        assertEquals(Parser.CommandType.C_COMMAND, parser.commandType("movw %A,%D"));
        assertEquals(Parser.CommandType.L_COMMAND, parser.commandType("TESTE:"));
        assertEquals(Parser.CommandType.A_COMMAND, parser.commandType("leaw $100,%A"));
        assertEquals(Parser.CommandType.L_COMMAND, parser.commandType("Z0:"));
        assertEquals(Parser.CommandType.C_COMMAND, parser.commandType("movw %D,%A"));
        assertEquals(Parser.CommandType.C_COMMAND, parser.commandType("jmp"));
        assertEquals(Parser.CommandType.C_COMMAND, parser.commandType("nop"));
    }
    
    /**
     * Testa a identificação correta da label para os comandos do tipo L
     */
    
    @Test
    public void testLabel() {
        assumeFalse(parser.label("TESTE:") == null);
        assumeFalse(parser.label("TESTE:").isEmpty());
        
        assertEquals("abc", parser.label("abc:"));
        assertEquals("TESTE", parser.label("TESTE:"));
        assertEquals("Z0", parser.label("Z0:"));
    }
    
    /**
     * Testa a identificação correta do símbolo para os comandos do tipo A 
     */
    
    @Test
    public void testSymbol() {
        assumeFalse(parser.symbol("leaw $0,%A") == null);
        assumeFalse(parser.symbol("leaw $0,%A").isEmpty());
        
        assertEquals("0", parser.symbol("leaw $0,%A"));
        assertEquals("i", parser.symbol("leaw $i,%A"));
        assertEquals("LOOP", parser.symbol("leaw $LOOP,%A"));
        assertEquals("12345", parser.symbol("leaw $12345,%A"));
    }
    
    /**
     * Teste a criação correta do vetor de mnemônicos, a "instrução", para os comandos do tipo C  
     * 
     */
    
    @Test
    public void testInstruction() {
        //assumeFalse(parser.instruction("nop:") == null);
        
        assertArrayEquals(new String[] { "leaw", "$0", "%A" }, parser.instruction("leaw $0,%A"));
        assertArrayEquals(new String[] { "leaw", "$i", "%A" }, parser.instruction("leaw $i,%A"));
        
        assertArrayEquals(new String[] { "leaw", "$LOOP", "%A" },
                parser.instruction("leaw $LOOP,%A"));
        
        assertArrayEquals(new String[] { "leaw", "$12345", "%A" },
                parser.instruction("leaw $12345,%A"));
        
        assertArrayEquals(new String[] { "movw", "%A", "%D" }, parser.instruction("movw %A,%D"));
        assertArrayEquals(new String[] { "movw", "%D", "%A" }, parser.instruction("movw %D,%A"));
        
        assertArrayEquals(new String[] { "addw", "%S", "%A", "%D" },
                parser.instruction("addw %S,%A,%D"));
        
        assertArrayEquals(new String[] { "jmp" }, parser.instruction("jmp"));
        assertArrayEquals(new String[] { "nop" }, parser.instruction("nop"));
        assertArrayEquals(new String[] { "decw", "%A" }, parser.instruction("decw %A"));
        assertArrayEquals(new String[] { "decw", "%D" }, parser.instruction("decw %D"));
        assertArrayEquals(new String[] { "notw", "%S" }, parser.instruction("notw %S"));
        assertArrayEquals(new String[] { "notw", "%D" }, parser.instruction("notw %D"));
        assertArrayEquals(new String[] { "negw", "%A" }, parser.instruction("negw %A"));
        assertArrayEquals(new String[] { "negw", "%D" }, parser.instruction("negw %D"));
    }

    /**
     * Testa a leitura correta dos comandos do tipo "leaw"
     */

    @Test
    public void testReadLeaw() throws IOException {
        Parser parser = new Parser("src/test/resources/testLeaw.nasm");
        
        assumeTrue(parser.advance());
        
        assertEquals("leaw $0,%A", parser.command());
        assertTrue(parser.advance());
        assertEquals("leaw $1,%A", parser.command());
        assertTrue(parser.advance());
        assertEquals("leaw $2,%A", parser.command());
        assertTrue(parser.advance());
        assertEquals("leaw $R0,%A", parser.command());
        assertTrue(parser.advance());
        assertEquals("leaw $R1,%A", parser.command());
        assertTrue(parser.advance());
        assertEquals("leaw $R2,%A", parser.command());
        
        assertFalse(parser.advance());

        parser.close();
    }

    /**
     * Testa a leitura correta dos comandos do tipo "jump"
     */

    @Test
    public void testReadJump() throws IOException {
        Parser parser = new Parser("src/test/resources/testJump.nasm");
        
        assumeTrue(parser.advance());
        
        assertEquals("jmp", parser.command());
        assertTrue(parser.advance());
        assertEquals("nop", parser.command());
        assertTrue(parser.advance());
        assertEquals("jne", parser.command());
        assertTrue(parser.advance());
        assertEquals("nop", parser.command());
        assertTrue(parser.advance());
        assertEquals("je", parser.command());
        assertTrue(parser.advance());
        assertEquals("nop", parser.command());
        assertTrue(parser.advance());
        assertEquals("jge", parser.command());
        assertTrue(parser.advance());
        assertEquals("jg", parser.command());
        assertTrue(parser.advance());
        assertEquals("jle", parser.command());
        assertTrue(parser.advance());
        assertEquals("jl", parser.command());
        assertTrue(parser.advance());
        assertEquals("jmp", parser.command());
        
        assertFalse(parser.advance());

        parser.close();
    }
    
    /**
     * Testa a leitura correta dos comandos do tipo C
     */

    @Test
    public void testReadComputation() throws IOException {
        Parser parser = new Parser("src/test/resources/testComp.nasm");
        
        assumeTrue(parser.advance());
        
        assertEquals("movw %A,%D", parser.command());
        assertTrue(parser.advance());
        assertEquals("addw %A,%D,%D", parser.command());
        assertTrue(parser.advance());
        assertEquals("movw %D,%A", parser.command());
        assertTrue(parser.advance());
        assertEquals("movw %D,(%A)", parser.command());
        assertTrue(parser.advance());
        assertEquals("incw %D", parser.command());
        assertTrue(parser.advance());
        assertEquals("nop", parser.command());
        assertTrue(parser.advance());
        assertEquals("movw (%A),%D", parser.command());
        assertTrue(parser.advance());
        assertEquals("addw (%A),%D,%D", parser.command());
        assertTrue(parser.advance());
        assertEquals("subw %D,(%A),%A", parser.command());
        assertTrue(parser.advance());
        assertEquals("rsubw %D,(%A),%A", parser.command());
        assertTrue(parser.advance());
        assertEquals("decw %A", parser.command());
        assertTrue(parser.advance());
        assertEquals("decw %D", parser.command());
        assertTrue(parser.advance());
        assertEquals("notw %A", parser.command());
        assertTrue(parser.advance());
        assertEquals("notw %D", parser.command());
        assertTrue(parser.advance());
        assertEquals("negw %A", parser.command());
        assertTrue(parser.advance());
        assertEquals("negw %D", parser.command());
        assertTrue(parser.advance());
        assertEquals("andw (%A),%D,%D", parser.command());
        assertTrue(parser.advance());
        assertEquals("andw %D,%A,%A", parser.command());
        assertTrue(parser.advance());
        assertEquals("orw (%A),%D,%D", parser.command());
        assertTrue(parser.advance());
        assertEquals("orw %D,%A,%A", parser.command());
        
        assertFalse(parser.advance());

        parser.close();
    }
}