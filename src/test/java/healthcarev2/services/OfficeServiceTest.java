package healthcarev2.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import healthcarev2.model.Doctor;
import healthcarev2.model.Office;
import healthcarev2.repository.OfficeRepository;
import healthcarev2.service.OfficeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class OfficeServiceTest {
    @Mock
    private  OfficeRepository officeRepository;

    @InjectMocks
    private OfficeService officeService;

    private Office office;
    private Doctor doctor;

    @BeforeEach
    public void setup(){
        // Initialize a test Doctor
        doctor = new Doctor();
        doctor.setDoctorId(1);
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialty("Cardiology");
        doctor.setEmail("john.doe@example.com");

        office = new Office();
        office.setOfficeId(1);
        office.setLocation("123 Main St");
        office.setPhone("123-456-7890");
        office.setDoctor(doctor);
    }

    @Test
    void createOffice_shouldCallRepositoryAndReturnOffice() {
        // Arrange
        doNothing().when(officeRepository).create(office); // Mock the repository's create method to do nothing

        // Act
        Office createdOffice = officeService.createOffice(office);

        // Assert
        verify(officeRepository, times(1)).create(office);
        assertEquals(office, createdOffice);
    }

@Test
    public void updateOffice_shouldCallRepository(){
        officeService.updateOffice(office);
        verify(officeRepository, times(1)).update(office);
}

@Test
    public void deleteOffice_shouldCallRepositoryWithCorrectId(){
    // Act
        int id = office.getOfficeId();
        officeService.deleteOffice(id);
    // Assert
    verify(officeRepository,times(1)).delete(id);
}

@Test
    public void getOfficeById_shouldReturnCorrectOffice(){
    // Arrange
    int id = office.getOfficeId();
    when(officeRepository.findById(id)).thenReturn(office);
        // Act

    Office foundOffice = officeService.getOfficeById(id);
    // Assert
    verify(officeRepository,times(1)).findById(id);
    assertEquals(office,foundOffice);
}
@ParameterizedTest
@CsvSource({"Main Office", "Branch Office"})
    public void createOffice_withVariousNames_shouldWork(String officeName){
        //arrange
        office.setLocation(officeName);
        doNothing().when(officeRepository).create(office);
    Office createdOffice = officeService.createOffice(office);
    verify(officeRepository,times(1)).create(office);
     assertNotNull(office.getOfficeId());
     assertEquals(officeName,createdOffice.getLocation());
}
    @Test
    void createOffice_whenRepositoryThrowsException_shouldThrowException() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(officeRepository).create(office);

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> officeService.createOffice(office));
        assertEquals("Database error", exception.getMessage());
    }
       }