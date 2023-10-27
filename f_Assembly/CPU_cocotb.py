# Adapted from https://github.com/cocotb/cocotb/blob/master/examples/doc_examples/quickstart/test_my_design.py

import cocotb
from cocotb.triggers import Timer, FallingEdge
from cocotb.clock import Clock
import os
from termcolor import colored
        
        
@cocotb.test()
async def tb_CPU(dut):

    file_test = open("../config_testes_nasm.txt","r")
    lines_test = file_test.readlines() 
    file_test.close()

    print("\n\n===================================================")
    for line in lines_test:
        if len(line.strip()):
            if ((line.strip()[0] != '#') and ( line.strip().find('.nasm') > 0 )):
                # pega parametros e atribui caminhos globais
                # par[0] : Nome do teste (subpasta)
                # par[1] : quantidade de testes a serem executados
                # par[2] : tempo de simulação em ns
                par = line.rstrip().split()
                name = par[0][:-5]
                sTime = int(par[2])
                mif = "../bin/hack/"+name+".mif"

                # verifica se arquivo existe
                if os.path.isfile(mif):
                    # simulate
                    for test in range(0, int(par[1])):
                        ramIn = "../tests/" + name + "/" + name +"{}".format(test)+ "_in.mif"
                        ramTest = "../tests/" + name + "/" + name +"{}".format(test)+ "_tst.mif"

                        RAM = [0] * (16*1024+4800+2)
                        ROM = [0] * 32*1024
                        #Import initial conditions from file
                        file_in = open(mif,"r")
                        Lines_in = file_in.readlines() 
                        file_in.close()
                        
                        for l in Lines_in:
                            if ":" in l:
                                no_line = int( l.split(":")[0].strip() )
                                value = int( l.split(":")[1].replace(";", "").strip(),2 )
                                ROM[no_line] = value 

                        file_in = open(ramIn,"r")
                        Lines_in = file_in.readlines() 
                        file_in.close()
                        
                        for l in Lines_in:
                            if ":" in l:
                                no_line = int( l.split(":")[0].strip() )
                                value = int( l.split(":")[1].replace(";", "").strip(),2 )
                                RAM[no_line] = value  
                            
                        clock = Clock(dut.clock, 2, units="ns")
                        await cocotb.start(clock.start())   

                        dut.reset.value = 1
                        await FallingEdge(dut.clock)
                        await FallingEdge(dut.clock)
                        dut.reset.value = 0

                        for i in range( sTime ):
                            try:
                                if dut.writeM.value == 1:
                                    RAM[dut.addressM.value] = dut.outM.value
                                else:
                                    if int(dut.addressM.value) <= 21185:
                                        dut.inM.value = RAM[dut.addressM.value]
                            except:
                                pass
                            dut.instruction.value = ROM[dut.pcout.value]
                            await FallingEdge(dut.clock) 

                        #Verification final results
                        file_in = open(ramTest,"r")
                        Lines_in = file_in.readlines() 
                        file_in.close()

                        for l in Lines_in:
                            if ":" in l:
                                no_line = int( l.split(":")[0].strip() )
                                value = int( l.split(":")[1].replace(";", "").strip(),2 )

                                condition = (RAM[no_line] == value)
                                if not condition:
                                    dut._log.error("Expected value " + str(no_line) + ": " + "{0:016b}".format(value) + " Obtained value " + str(no_line) + ": " + "{0:016b}".format(int(RAM[no_line])) )
                                    print( 'Test {:15s}: '.format(name + "{}".format(test)) + colored('Failed', 'red'))
                                    assert condition, "Error in test " + name + "{}".format(test)
                                else:
                                    print( 'Test {:15s}: '.format(name + "{}".format(test)) + colored('Passed', 'green'))
    print("===================================================")
