package jpcompany.smartwire2.controller;

import jpcompany.smartwire2.service.dto.MachineSetupCommand;
import jpcompany.smartwire2.controller.dto.response.ResponseDto;
import jpcompany.smartwire2.domain.Member;
import jpcompany.smartwire2.service.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MachineController {

    private final MachineService machineService;

    @PostMapping("/machine/setup")
    public ResponseEntity<ResponseDto> setupMachines(
            @AuthenticationPrincipal Member member,
            @Validated @RequestBody List<MachineSetupCommand> machinesForm
    ) {
        machineService.save(machinesForm, member.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(true, null, null));
    }
}