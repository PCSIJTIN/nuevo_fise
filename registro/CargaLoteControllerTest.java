package gob.osinergmin.fise.controller.formatos.registro;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;

import gob.osinergmin.fise.domain.Empresa;
import gob.osinergmin.fise.domain.Usuario;
import gob.osinergmin.fise.domain.dto.LoteDTO;
import gob.osinergmin.fise.domain.dto.TipoEmpresaDTO;
import gob.osinergmin.fise.domain.dto.filters.FiltroLoteDTO;
import gob.osinergmin.fise.domain.dto.filters.FiltroSolicitudesDTO;
import gob.osinergmin.fise.service.EmpresaTipoEmpresaService;
import gob.osinergmin.fise.service.LoteService;
import gob.osinergmin.fise.util.FISEPropertiesEnum;
import gob.osinergmin.fise.util.JqGridUtil;
import gob.osinergmin.fise.util.PropertiesUtils;
import gob.osinergmin.fise.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropertiesUtils.class , ResponseUtil.class})
public class CargaLoteControllerTest {
	
	
	{
		PowerMockito.mockStatic(PropertiesUtils.class);
		
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.FORMATO_FISE01.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.FORMATO_FISE02.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.FORMATO_FISE03.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.FORMATO_FISE04.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.FORMATO_FISE06.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.FORMATO_FISE07.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.FORMATO_FISE09.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.FORMATO_FISE10.getValue())).thenReturn("0");
		
	}
	
	@Mock
	private LoteService loteService;
	
	@Mock
	private EmpresaTipoEmpresaService empresaTipoEmpresaService;
	
	@InjectMocks
	CargaLoteController cargaLoteController  = new CargaLoteController();
	
	@Test
	public void prepararAdjuntarSuccess() throws Exception{
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		Model model = mock(Model.class);
		
		Usuario usuario = new Usuario();
		usuario.setEmpresa(new Empresa());
		
		FiltroLoteDTO filtroLoteDTO = new FiltroLoteDTO();
		filtroLoteDTO.setIdLoteTrabajar(10);
		
		LoteDTO loteDTO = new LoteDTO();
		loteDTO.setIdLote(10);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("filtrosLote")).thenReturn(filtroLoteDTO);
		when(loteService.obtenerLote(anyInt())).thenReturn(loteDTO);
		when(session.getAttribute("usuario")).thenReturn(usuario);
		when(empresaTipoEmpresaService.obtenerTipoEmpresaPorEmpresa(anyInt())).thenReturn(new TipoEmpresaDTO());
		
		String result = cargaLoteController.prepararAdjuntarArchivo(model, request);
		assertNotNull(result);
		assertEquals("formatos/registro/adjuntarArchivoLote", result);
	}
	
	@Test
	public void adjuntarArchivoTest() throws IOException{
		PowerMockito.mockStatic(ResponseUtil.class);

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response= mock(HttpServletResponse.class);
		MultipartFile file = mock(MultipartFile.class);
		when(file.getOriginalFilename()).thenReturn("Archivo.pdf");
		when(file.getBytes()).thenReturn(new byte[100]);
		cargaLoteController.adjuntarArchivo(file, 10, response, request);
		
		String resultado = "{\"model\":{\"nombreArchivo\":\"Archivo.pdf\",\"numeroLote\":0,\"success\":true,\"erroresProceso\":null,\"genericError\":null}}";
		PowerMockito.verifyStatic(times(1));
		ResponseUtil.escribirEnResponse(response,resultado);
	}
}
