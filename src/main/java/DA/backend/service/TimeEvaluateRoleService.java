package DA.backend.service;

import DA.backend.entity.Evaluate;
import DA.backend.entity.Role;
import DA.backend.entity.TimeEvaluateRole;
import DA.backend.repository.EvaluateRepository;
import DA.backend.repository.RoleRepository;
import DA.backend.repository.TimeEvaluateRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeEvaluateRoleService {
    @Autowired
    TimeEvaluateRoleRepository timeEvaluateRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    EvaluateRepository evaluateRepository;
    public void addTimeEvaluateRole(List<TimeEvaluateRole> timeEvaluateRoles) {
        for (TimeEvaluateRole timeEvaluateRole : timeEvaluateRoles) {
            // Xử lý Role
            Role role = timeEvaluateRole.getRole();
            if (role != null && role.getId() == null) {
                roleRepository.save(role); // Lưu Role trước nếu chưa có ID
            } else if (role != null) {
                // Nếu Role đã tồn tại, nạp từ cơ sở dữ liệu để quản lý
                role = roleRepository.findById(role.getId()).orElse(role);
                timeEvaluateRole.setRole(role);
            }

            // Xử lý Evaluate
            Evaluate evaluate = timeEvaluateRole.getEvaluate();
            if (evaluate != null && evaluate.getId() == null) {
                evaluateRepository.save(evaluate); // Lưu Evaluate trước nếu chưa có ID
            } else if (evaluate != null) {
                // Nếu Evaluate đã tồn tại, nạp từ cơ sở dữ liệu để quản lý
                evaluate = evaluateRepository.findById(evaluate.getId()).orElse(evaluate);
                timeEvaluateRole.setEvaluate(evaluate);
            }
        }

        // Sau khi các Role và Evaluate liên quan đã được lưu, lưu TimeEvaluateRole
        timeEvaluateRoleRepository.saveAll(timeEvaluateRoles);
    }

    public void updateTimeEvaluateRole(TimeEvaluateRole timeEvaluateRole){
        Optional<TimeEvaluateRole> optionalTimeEvaluateRole = timeEvaluateRoleRepository.findById(timeEvaluateRole.getId());
        if(optionalTimeEvaluateRole.isPresent()){
            TimeEvaluateRole timeEvaluateRole1 = optionalTimeEvaluateRole.get();
            timeEvaluateRole1.setRoles(timeEvaluateRole.getRoles());
            timeEvaluateRole1.setEndDay(timeEvaluateRole.getEndDay());
            timeEvaluateRole1.setStartDay(timeEvaluateRole.getStartDay());
        }else {
            timeEvaluateRoleRepository.save(timeEvaluateRole);
        }
    }
    public List<TimeEvaluateRole> list(){
        return timeEvaluateRoleRepository.findAll();
    }
}
