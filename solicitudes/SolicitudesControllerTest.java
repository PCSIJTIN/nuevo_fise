package gob.osinergmin.fise.controller.solicitudes;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import gob.osinergmin.fise.domain.dto.filters.FiltroAprobacionDTO;
import gob.osinergmin.fise.domain.dto.filters.FiltroSolicitudesDTO;
import gob.osinergmin.fise.security.entity.FiseUser;
import gob.osinergmin.fise.security.util.SecurityUtil;
import gob.osinergmin.fise.service.SolicitudesService;
import gob.osinergmin.fise.util.JqGridUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SecurityUtil.class })
public class SolicitudesControllerTest {
	@InjectMocks
	SolicitudesController solicitudesController  = new SolicitudesController();
	
	@Mock
    private SolicitudesService solicitudesService;
	
	@Test
	public void buscarSuccess(){
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		PowerMockito.mockStatic(SecurityUtil.class);
		
		when(SecurityUtil.getPrincipal()).thenReturn(new FiseUser(1, "user", "pass", "", "", 1, "", false, false, false, false, new ArrayList(), new ArrayList()));
		
		solicitudesController.buscar(request, response, -1, "", -1, true, "15/01/2015", "15/01/2015", true, "15/01/2015", "15/01/2015", null, 1, 10, "http://localhost:9080/SistemaIntegralFise/pages/inicio", "asc");
		verify(solicitudesService, times(1)).listRequerimientos(any(FiltroSolicitudesDTO.class));
		verify(solicitudesService, times(1)).countRequerimientos(any(FiltroSolicitudesDTO.class));
	}
}
