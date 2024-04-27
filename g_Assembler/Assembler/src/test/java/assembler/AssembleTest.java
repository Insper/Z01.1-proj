/**
 * Curso: Elementos de Sistemas
 * Arquivo: CodeTest.java
 * Created by Lucas Hix <lucash@al.insper.edu.br>
 * Date: 21/11/2022
 */

 package assembler;

 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.junit.jupiter.api.Assertions.assertTrue;
 
 import java.io.BufferedReader;
 import java.io.FileReader;
 import java.io.IOException;
 
 import org.junit.jupiter.api.BeforeAll;
 import org.junit.jupiter.api.Test;
 
 public class AssembleTest {
     
     private static final String inFile = "src/test/resources/isEven.nasm";
     private static final String outFile = "src/test/resources/isEven.hack";
     
     private static Assemble assembler;
     
     @BeforeAll
     public static void setUp() throws IOException {
         assembler = new Assemble(inFile, outFile, true);
     }
     
     /**
      * Teste da indetificação e armazenamento das labels, CommandType#L_COMMAND
      * 
      * @throws IOException If any I/O error occurs
      */
     
     @Test
     public void testFillSymbolTable() throws IOException {
         // Cria tabela de símbolos
         SymbolTable table = assembler.fillSymbolTable();
         
         assertTrue(table.contains("impar"));
         assertEquals(12, (int) table.getAddress("impar")); // $impar
 
         assertTrue(table.contains("par"));
         assertEquals(2, (int) table.getAddress("par")); // $par
         
         assertTrue(table.contains("end"));
         assertEquals(14, (int) table.getAddress("end")); // $end
     }
     
     /**
      * Teste da geração do código de maquina
      * 
      * @throws IOException If any I/O error occurs
      */
     
     @Test
     public void testGenerateMachineCode() throws IOException {
         assembler.fillSymbolTable();
         assembler.generateMachineCode();
         assembler.close();
         
         BufferedReader fileReader = new BufferedReader(new FileReader(outFile));
         
         assertEquals("000000000000000101", fileReader.readLine()); // leaw $5, %A
         assertEquals("100011100000010000", fileReader.readLine()); // movw (%A), %D
         assertEquals("000000000000000001", fileReader.readLine()); // leaw $1, %A
         assertEquals("100000000000010000", fileReader.readLine()); // andw %A, %D, %D
         assertEquals("000000000000001100", fileReader.readLine()); // leaw $impart, %A
         assertEquals("100000011000000001", fileReader.readLine()); // jg
         assertEquals("100000011000000000", fileReader.readLine()); // nop "100000011000000000"
         assertEquals("000000000000000000", fileReader.readLine()); // leaw $0, %A
         assertEquals("100001111110100000", fileReader.readLine()); // movw $1, ($A)
         assertEquals("000000000000001110", fileReader.readLine()); // leaw $impart, %A
         assertEquals("100000011000000111", fileReader.readLine()); // jg
         assertEquals("100000011000000000", fileReader.readLine()); // nop "100000011000000000"
         assertEquals("000000000000000000", fileReader.readLine()); // leaw $0, %A
         assertEquals("100001010100100000", fileReader.readLine()); // movw $0, ($A)
         
         fileReader.close();
     }
 }