package DA.backend.service;

import DA.backend.entity.Evaluate;
import DA.backend.entity.Role;
import DA.backend.entity.TimeEvaluateRole;
import DA.backend.repository.TimeEvaluateRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TimeEvaluateRoleService {

    @Autowired
    TimeEvaluateRoleRepository timeEvaluateRoleRepository;

    public void addTimeEvaluateRole(List<TimeEvaluateRole> timeEvaluateRoles) {
        timeEvaluateRoleRepository.saveAll(timeEvaluateRoles);

    }


    public void updateTimeEvaluateRole(List<TimeEvaluateRole> timeEvaluateRoles) {
        for (TimeEvaluateRole timeEvaluateRole : timeEvaluateRoles) {
            // Kiểm tra nếu ID của timeEvaluateRole không phải là null
            if (timeEvaluateRole.getId() != null) {
                Optional<TimeEvaluateRole> optionalTimeEvaluateRole = timeEvaluateRoleRepository.findById(timeEvaluateRole.getId());

                if (optionalTimeEvaluateRole.isPresent()) {
                    TimeEvaluateRole existingTimeEvaluateRole = optionalTimeEvaluateRole.get();

                    // Cập nhật các trường của TimeEvaluateRole từ đối tượng được truyền vào
                    existingTimeEvaluateRole.setRole(timeEvaluateRole.getRole());
                    existingTimeEvaluateRole.setEndDay(timeEvaluateRole.getEndDay());
                    existingTimeEvaluateRole.setStartDay(timeEvaluateRole.getStartDay());
                    existingTimeEvaluateRole.setEvaluate(timeEvaluateRole.getEvaluate());

                    // Lưu lại đối tượng đã cập nhật vào cơ sở dữ liệu
                    timeEvaluateRoleRepository.save(existingTimeEvaluateRole);
                } else {
                    // Nếu đối tượng không tồn tại trong cơ sở dữ liệu, báo lỗi hoặc bỏ qua
                    System.out.println("TimeEvaluateRole với ID " + timeEvaluateRole.getId() + " không tồn tại trong cơ sở dữ liệu.");
                }
            } else {
                // ID của đối tượng là null, bỏ qua hoặc xử lý lỗi nếu cần
                System.out.println("ID của TimeEvaluateRole là null, không thể cập nhật.");
            }
        }
    }



    public List<TimeEvaluateRole> list() {
        return timeEvaluateRoleRepository.findAll();
    }
}
