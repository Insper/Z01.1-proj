from cocotb_test.simulator import run
import pytest
import os

from telemetry import telemetryMark
pytestmark = telemetryMark()


def source(name):
	dir = os.path.dirname(__file__)
	src_dir = os.path.join(dir, 'src' )
	return os.path.join(src_dir, name)


@pytest.mark.telemetry_files(source('../../e_CPU/src/cpu.vhd'),source('../../e_CPU/src/controlunit.vhd'),source('../../b_logComb/src/mux16.vhd'),source('../../c_ULA/src/alu.vhd'),source('../../d_logSeq/src/register16.vhd'),source('../../d_logSeq/src/pc.vhd'),source('../../d_logSeq/src/register8.vhd'),source('../../d_logSeq/src/binarydigit.vhd'),source('../../d_logSeq/src/flipflopd.vhd'),source('../../b_logComb/src/mux2way.vhd'),source("../../c_ULA/src/zerador16.vhd"), source("../../c_ULA/src/inversor16.vhd"), source("../../c_ULA/src/add16.vhd"), source("../../c_ULA/src/fulladder.vhd"), source("../../b_logComb/src/and16.vhd"), source("../../c_ULA/src/comparador16.vhd"), source("../../c_ULA/src/inc16.vhd"))
def test_cpu():
    run(vhdl_sources=[source('../../e_CPU/src/cpu.vhd'),source('../../e_CPU/src/controlunit.vhd'),source('../../b_logComb/src/mux16.vhd'),source('../../c_ULA/src/alu.vhd'),source('../../d_logSeq/src/register16.vhd'),source('../../d_logSeq/src/pc.vhd'),source('../../d_logSeq/src/register8.vhd'),source('../../d_logSeq/src/binarydigit.vhd'),source('../../d_logSeq/src/flipflopd.vhd'),source('../../b_logComb/src/mux2way.vhd'),source("../../c_ULA/src/zerador16.vhd"), source("../../c_ULA/src/inversor16.vhd"), source("../../c_ULA/src/add16.vhd"), source("../../c_ULA/src/fulladder.vhd"), source("../../b_logComb/src/and16.vhd"), source("../../c_ULA/src/comparador16.vhd"), source("../../c_ULA/src/inc16.vhd")], toplevel="cpu", module="CPU_cocotb" , testcase='tb_CPU', toplevel_lang="vhdl")


if __name__ == "__main__":
    test_cpu()

