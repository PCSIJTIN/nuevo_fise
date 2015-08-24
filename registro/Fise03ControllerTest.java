package gob.osinergmin.fise.controller.formatos.registro;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gob.osinergmin.fise.domain.Empresa;
import gob.osinergmin.fise.domain.Usuario;
import gob.osinergmin.fise.domain.dto.ArchivoDTO;
import gob.osinergmin.fise.domain.dto.Fise03DTO;
import gob.osinergmin.fise.domain.dto.LoteDTO;
import gob.osinergmin.fise.domain.dto.TipoEmpresaDTO;
import gob.osinergmin.fise.domain.dto.filters.FiltroFormatoPeriodoDTO;
import gob.osinergmin.fise.service.EmpresaTipoEmpresaService;
import gob.osinergmin.fise.service.ExcepcionRegistroService;
import gob.osinergmin.fise.service.Fise03Service;
import gob.osinergmin.fise.service.FormatoPeriodoService;
import gob.osinergmin.fise.service.LoteService;
import gob.osinergmin.fise.util.FISEPropertiesEnum;
import gob.osinergmin.fise.util.PropertiesUtils;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropertiesUtils.class})
public class Fise03ControllerTest {
	
	{
		PowerMockito.mockStatic(PropertiesUtils.class);
		
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.FORMATO_FISE03.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.TIPO_LOTE_MANUAL.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.ESTADO_EXCEPCION_PENDIENTE.getValue())).thenReturn("0");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.ESTADO_ACTIVO.getValue())).thenReturn("0");
		when(PropertiesUtils.getMessagesProperty("fise03.excepcion.enviar")).thenReturn("error");
		when(PropertiesUtils.getAppProperty(FISEPropertiesEnum.TOPE_LISTADO_ERRORES_CARGA_LOTE.getValue())).thenReturn("0");
		
	}
	
	@InjectMocks
	Fise03Controller fise03Controller = new Fise03Controller();
	
	@Mock
	private LoteService loteService;
	
	@Mock
	private EmpresaTipoEmpresaService empresaTipoEmpresaService;
	
	@Mock
	private Fise03Service fise03Service;
	
	@Mock
    private FormatoPeriodoService formatoPeriodoService;
	
	@Mock
    private ExcepcionRegistroService excepcionRegistroService;
	
	@Test
	public void enviarLoteManualConId() throws Exception{
		DefaultMultipartHttpServletRequest request = mock(DefaultMultipartHttpServletRequest.class);
		MultiValueMap<String, MultipartFile> multiPartRequest = mock(MultiValueMap.class);
		MultipartFile file = mock(MultipartFile.class);
		HttpSession session = mock(HttpSession.class);
		Usuario usuario = new Usuario();
		Empresa empresa = new Empresa();
		empresa.setIdEmpresa(1);
		usuario.setEmpresa(empresa);
		
		String itemsParameter = "[{\"idLote\":\"1\", \"idEmpresa\":\"1\"}]";
		String idPeriodoParam = "1" ;
		String idLoteParam = "1";
		
		LoteDTO loteDTO = new LoteDTO();
    	loteDTO.setIdLote(1);
    	loteDTO.setIdPeriodo(1);
    	loteDTO.setIdEmpresa(1);
    	loteDTO.setIdTipoEmpresa(1);
    	loteDTO.setIdUsuario(1);
    	loteDTO.setTerminal("1");
    	loteDTO.setIdFormato(0);
    	loteDTO.setTipoLote("0");
		
    	TipoEmpresaDTO tipoEmpresaDTO = new TipoEmpresaDTO();
    	tipoEmpresaDTO.setIdTipoEmpresa(1);
    			
		
		when(request.getMultiFileMap()).thenReturn(multiPartRequest);
		when(request.getParameter("items")).thenReturn(itemsParameter);
		when(request.getParameter("idPeriodo")).thenReturn(idPeriodoParam);
		when(request.getParameter("idLote")).thenReturn(idLoteParam);
		when(multiPartRequest.getFirst("archivo")).thenReturn(file);
		
		when(file.getOriginalFilename()).thenReturn("Archivo.pdf");
		when(file.getBytes()).thenReturn(new byte[100]);
		when(file.getSize()).thenReturn(100L);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("usuario")).thenReturn(usuario);
		when(loteService.obtenerLote(anyInt())).thenReturn(loteDTO);
		when(empresaTipoEmpresaService.obtenerTipoEmpresaPorEmpresa(anyInt())).thenReturn(tipoEmpresaDTO);
		when(formatoPeriodoService.getCantidadFormatosPeriodoAbiertos((FiltroFormatoPeriodoDTO)anyObject())).thenReturn((long) 1);
		when(fise03Service.enviarLoteManual((LoteDTO)anyObject(), (List<Fise03DTO>)anyObject(), (ArchivoDTO)anyObject())).thenReturn(1);
		
		Map<String, ? extends Object>  result = fise03Controller.enviarLoteManual(request);
		assertNotNull(result);
		assertNotNull(result.get("idLote"));
		assertEquals(1, result.get("idLote"));
	}
	
	@Test
	public void enviarLoteManualSinId() throws Exception{
		DefaultMultipartHttpServletRequest request = mock(DefaultMultipartHttpServletRequest.class);
		MultiValueMap<String, MultipartFile> multiPartRequest = mock(MultiValueMap.class);
		MultipartFile file = mock(MultipartFile.class);
		HttpSession session = mock(HttpSession.class);
		Usuario usuario = new Usuario();
		Empresa empresa = new Empresa();
		empresa.setIdEmpresa(1);
		usuario.setEmpresa(empresa);
		
		String itemsParameter = "[{\"idLote\":\"1\", \"idEmpresa\":\"1\"}]";
		String idPeriodoParam = "1" ;
		
		LoteDTO loteDTO = new LoteDTO();
    	loteDTO.setIdLote(1);
    	loteDTO.setIdPeriodo(1);
    	loteDTO.setIdEmpresa(1);
    	loteDTO.setIdTipoEmpresa(1);
    	loteDTO.setIdUsuario(1);
    	loteDTO.setTerminal("1");
    	loteDTO.setIdFormato(0);
    	loteDTO.setTipoLote("0");
		
    	TipoEmpresaDTO tipoEmpresaDTO = new TipoEmpresaDTO();
    	tipoEmpresaDTO.setIdTipoEmpresa(1);
    			
		
		when(request.getMultiFileMap()).thenReturn(multiPartRequest);
		when(request.getParameter("items")).thenReturn(itemsParameter);
		when(request.getParameter("idPeriodo")).thenReturn(idPeriodoParam);
		when(request.getParameter("idLote")).thenReturn(null);
		when(multiPartRequest.getFirst("archivo")).thenReturn(file);
		
		when(file.getOriginalFilename()).thenReturn("Archivo.pdf");
		when(file.getBytes()).thenReturn(new byte[100]);
		when(file.getSize()).thenReturn(100L);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("usuario")).thenReturn(usuario);
		when(loteService.obtenerLote(anyInt())).thenReturn(loteDTO);
		when(empresaTipoEmpresaService.obtenerTipoEmpresaPorEmpresa(anyInt())).thenReturn(tipoEmpresaDTO);
		when(formatoPeriodoService.getCantidadFormatosPeriodoAbiertos((FiltroFormatoPeriodoDTO)anyObject())).thenReturn((long) 1);
		when(fise03Service.enviarLoteManual((LoteDTO)anyObject(), (List<Fise03DTO>)anyObject(), (ArchivoDTO)anyObject())).thenReturn(1);
		
		Map<String, ? extends Object>  result = fise03Controller.enviarLoteManual(request);
		assertNotNull(result);
		assertNotNull(result.get("idLote"));
		assertEquals(1, result.get("idLote"));
	}
	
	@Test
	public void enviarLoteManualError() throws Exception{
		DefaultMultipartHttpServletRequest request = mock(DefaultMultipartHttpServletRequest.class);
		MultiValueMap<String, MultipartFile> multiPartRequest = null;
		HttpSession session = mock(HttpSession.class);
		Usuario usuario = new Usuario();
		Empresa empresa = new Empresa();
		empresa.setIdEmpresa(1);
		usuario.setEmpresa(empresa);
		
		String itemsParameter = "[{\"idLote\":\"1\", \"idEmpresa\":\"1\"}]";
		String idPeriodoParam = "1" ;
		
		
		when(request.getMultiFileMap()).thenReturn(multiPartRequest);
		when(request.getParameter("items")).thenReturn(itemsParameter);
		when(request.getParameter("idPeriodo")).thenReturn(idPeriodoParam);
		when(request.getParameter("idLote")).thenReturn(null);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("usuario")).thenReturn(usuario);
		when(fise03Service.enviarLoteManual((LoteDTO)anyObject(), (List<Fise03DTO>)anyObject(), (ArchivoDTO)anyObject())).thenReturn(1);
		
		Map<String, ? extends Object>  result = fise03Controller.enviarLoteManual(request);
		assertNotNull(result);
		assertNotNull(result.get("error"));
		assertEquals("error", result.get("error"));
	}
}

