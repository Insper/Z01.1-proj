# -*- coding: utf-8 -*-
# Rafael Corsi @ insper.edu.br
# Dez/2017
# Disciplina Elementos de Sistemas
#
# script para gerar hack a partir de nasm
# suporta como entrada um único arquivo
# ou um diretório
# Possibilita também a geração do .mif

import os,sys,argparse, subprocess, re
from os.path import basename
from termcolor import colored

TAB = "    "
END = "\n"

cAssembler = 'magenta'
cError     = 'red'

ERRO_NONE = 0
ERRO_ASSEMBLER = 1
ERRO_ASSEMBLER_FILE = 2

def logAssembler(s):
        print(colored(s,cAssembler))

def logError(s):
    print(colored(s,cError))

def toMIF(mem, mif):
    cnt = 0
    try:
        fw = open(mif,"w")
        fr = open(mem,"r")

        # verifica quantas instrucoes possui
        # 11/4/18
        num_lines = sum(1 for line in open(mem))

        fw.write("-- Elementos de Sistema - INSPER.edu.br"+END)
        fw.write("-- Rafael Corsi"+END)
        fw.write("-- File generated by toMIF.py"+END)
        fw.write("-- originated from"+mem+""+END)
        fw.write("-- to be used on ALTERA FPGAs"+END+END)

        fw.write("WIDTH=18;"+END)
        fw.write("DEPTH={};".format(num_lines)+END)
        fw.write(""+END)
        fw.write("ADDRESS_RADIX=UNS;"+END)
        fw.write("DATA_RADIX=BIN;"+END)
        fw.write(""+END)
        fw.write("CONTENT BEGIN"+END)

        for line in fr:
            fw.write( TAB
                      + '{:4d}'.format(cnt)
                      +" : "
                      +line.rstrip()
                      +";"
                      +""+END)
            cnt = cnt + 1
        # colocar for aqui
        fw.write("END;"+END)

        fw.close()
        fr.close()

    except IOError:
        print("Arquivo não encontrado")

def compileAll(jar, nasm, hack):
    i = 0; erro = 0;
    print(" 1/2 Removendo arquivos antigos .hack" )
    print("  - {}".format(hack))
    clearbin(hack)

    print(" 2/2 Gerando novos arquivos   .hack")
    for n in nasm:
        print("  - {}".format(n))
        e, l = assemblerAll(jar, n, hack, True)
        erro += e;
    return e, l


def callJava(jar, nasm, hack):
    command = "java -jar " + jar + " -i " + nasm + " -o " + hack
    proc = subprocess.Popen(command, shell=True)
    err = proc.wait()
    return(err)


def clearbin(hack):
    try:
        shutil.rmtree(hack)
    except:
        pass


def assemblerAll(jar, nasm, hack, mif):

    error = -1
    log = []

    pwd = os.path.dirname(os.path.abspath(__file__))

    # global path
    os.path.abspath(nasm)
    os.path.abspath(hack)

    if not os.path.exists(os.path.dirname(hack)):
        os.makedirs(os.path.dirname(hack))

    if(os.path.isdir(nasm)):
        if(os.path.isdir(hack)):
            for filename in os.listdir(nasm):
                status = 'true'
                if (filename.strip().find('.nasm') > 0):
                    nHack = hack+filename[:-5]+".hack"
                    nMif  = hack+filename[:-5]+".mif"
                    nNasm = nasm+filename
                    if not os.path.basename(nNasm).startswith('.'):
                        e, l = assemblerFile(jar, nNasm, nHack, nMif)
                        log.append(l)
                        if e > 0:
                            return ERRO_ASSEMBLER, log
        else:
            logError("output must be folder for folder input!")
            return ERRO_ASSEMBLER_FILE, log
    return ERRO_NONE, log


def assemblerFile(jar, nasm, hack, mif):
    error = ERRO_NONE

    if not os.path.exists(os.path.dirname(hack)):
        os.makedirs(os.path.dirname(hack))

    hack = hack
    print("   - {} to {}".format(os.path.basename(nasm), os.path.basename(hack)))
    if callJava(jar, nasm, hack) != 0:
        status = 'Assembler Fail'
        error  = ERRO_ASSEMBLER
    else:
        status = 'Assembler Ok'
        error = ERRO_NONE
    if mif:
        toMIF(hack, os.path.splitext(hack)[0]+".mif")
    log = ({'name': os.path.basename(os.path.splitext(hack)[0]), 'status': status})

    return error, log

def callJava_VM(jar, vm, nasm, bootstrap=False):

    command = "java -jar " + jar + " " + vm + " -o " + nasm
    if not bootstrap:
        command += " -n"
    proc = subprocess.Popen(command, stdout=subprocess.PIPE, shell=True)
    err = proc.wait()
    return(err)
    
def vmtranslator(bootstrap, vmDir, nasm, jar):

    if not os.path.exists(os.path.dirname(nasm)):
        os.makedirs(os.path.dirname(nasm))

    if not isinstance(vmDir, list):
        vmDir = [vmDir, '']

    for vm in vmDir:
        if(vm != ''):
            if(os.path.isdir(nasm)):
                for filename in os.listdir(vm):
                    print(filename)
                    if(os.path.isdir(vm+'/'+filename)):
                        nNasm = nasm+filename+".nasm"
                    else:
                        nNasm = nasm+filename[:-3]+".nasm"
                    nVM = vm+filename
                    if not os.path.basename(nVM).startswith('.'):
                        print("Compiling {} to {}".format(os.path.basename(nVM), os.path.basename(nNasm)))
                        rtn = callJava_VM(jar, nVM, nNasm, bootstrap)
                        if(rtn > 0):
                            return(rtn)
            else:
                logError("output must be folder for folder input!")