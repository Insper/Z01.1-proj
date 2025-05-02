/**
 * Curso: Elementos de Sistemas
 * Arquivo: CodeTest.java
 * Created by Lucas Hix <lucash@al.insper.edu.br>
 * Date: 21/11/2022
 */

package assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SymbolTableTest {
    
    private static SymbolTable table;
    
    @BeforeAll
    public static void setUp() {
        table = new SymbolTable();
        
        table.addEntry("X", 0);
    }
    
    /**
     * Teste para verificar a existência dos símbolos padrões na inicializção do sistema
     */
    
    @Test
    public void testInitialization() {
        assumeTrue(table.contains("X"));
        
        assertTrue(table.contains("R0"));
        assertEquals(0, table.getAddress("R0"));
        
        assertTrue(table.contains("R1"));
        assertEquals(1, table.getAddress("R1"));
        
        assertTrue(table.contains("R2"));
        assertEquals(2, table.getAddress("R2"));
        
        assertTrue(table.contains("R3"));
        assertEquals(3, table.getAddress("R3"));
        
        assertTrue(table.contains("R4"));
        assertEquals(4, table.getAddress("R4"));
        
        assertTrue(table.contains("R5"));
        assertEquals(5, table.getAddress("R5"));
        
        assertTrue(table.contains("R6"));
        assertEquals(6, table.getAddress("R6"));
        
        assertTrue(table.contains("R7"));
        assertEquals(7, table.getAddress("R7"));
        
        assertTrue(table.contains("R8"));
        assertEquals(8, table.getAddress("R8"));
        
        assertTrue(table.contains("R9"));
        assertEquals(9, table.getAddress("R9"));
        
        assertTrue(table.contains("R10"));
        assertEquals(10, table.getAddress("R10"));
        
        assertTrue(table.contains("R11"));
        assertEquals(11, table.getAddress("R11"));
        
        assertTrue(table.contains("R12"));
        assertEquals(12, table.getAddress("R12"));
        
        assertTrue(table.contains("R13"));
        assertEquals(13, table.getAddress("R13"));
        
        assertTrue(table.contains("R14"));
        assertEquals(14, table.getAddress("R14"));
        
        assertTrue(table.contains("R15"));
        assertEquals(15, table.getAddress("R15"));
        
        assertTrue(table.contains("SCREEN"));
        assertEquals(16384, table.getAddress("SCREEN"));
        
        assertTrue(table.contains("SP"));
        assertEquals(0, table.getAddress("SP"));
        
        assertTrue(table.contains("LCL"));
        assertEquals(1, table.getAddress("LCL"));
        
        assertTrue(table.contains("ARG"));
        assertEquals(2, table.getAddress("ARG"));
        
        assertTrue(table.contains("THIS"));
        assertEquals(3, table.getAddress("THIS"));
        
        assertTrue(table.contains("THAT"));
        assertEquals(4, table.getAddress("THAT"));
    }
    
    /**
     * Testa a adição e obtenção de um unico símbolo
     */
    
    @Test
    public void testAddAndReadSingleEntry() {
        assumeTrue(table.contains("X"));
        
        assertFalse(table.contains("A"));
        table.addEntry("A", 127);
        assertTrue(table.contains("A"));
        assertEquals(127, table.getAddress("A"));
        
        assertFalse(table.contains("i"));
        table.addEntry("i", 16);
        assertTrue(table.contains("i"));
        assertEquals(16, table.getAddress("i"));
        
        assertFalse(table.contains("WxYz"));
        table.addEntry("WxYz", 16383);
        assertTrue(table.contains("WxYz"));
        assertEquals(16383, table.getAddress("WxYz"));
        
        assertFalse(table.contains("_123"));
        table.addEntry("_123", 123);
        assertTrue(table.contains("_123"));
        assertEquals(123, table.getAddress("_123"));
        
        assertFalse(table.contains("LOOP"));
        table.addEntry("LOOP", 0);
        assertTrue(table.contains("LOOP"));
        assertEquals(0, table.getAddress("LOOP"));
    }
    
    /**
     * Testa a adição e obtenção de um multiplos símbolos com valores repetidos
     */
    
    @Test
    public void testAddAndReadMultipleEntries() {
        assumeTrue(table.contains("X"));
        
        for (int i = 0; i < 16384; i++) {
            table.addEntry("TESTE" + i, 0);
        }
        for (int i = 0; i < 16384; i++) {
            assertTrue(table.contains("TESTE" + i));
        }
        for (int i = 0; i < 16384; i++) {
            assertEquals(0, table.getAddress("TESTE" + i));
        }
        for (int i = 0; i < 16384; i++) {
            table.addEntry("END" + i, 16383);
        }
        for (int i = 0; i < 16384; i++) {
            assertTrue(table.contains("END" + i));
        }
        for (int i = 0; i < 16384; i++) {
            assertEquals(16383, table.getAddress("END" + i));
        }
    }
}