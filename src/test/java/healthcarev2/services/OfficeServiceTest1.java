package healthcarev2.services;

import healthcarev2.impl.DoctorRepositoryImpl;
import healthcarev2.impl.OfficeRepositoryImpl;
import healthcarev2.model.Doctor;
import healthcarev2.model.Office;
import healthcarev2.repository.DoctorRepository;
import healthcarev2.repository.OfficeRepository;
import healthcarev2.service.DoctorService;
import healthcarev2.service.OfficeService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

public class OfficeServiceTest1 {
    private OfficeService officeService;
    private DoctorService doctorService;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();

        // Repositories
        OfficeRepository officeRepository = new OfficeRepositoryImpl(sessionFactory);
        DoctorRepository doctorRepository = new DoctorRepositoryImpl(sessionFactory);


        // Services
        officeService = new OfficeService(officeRepository);
        doctorService = new DoctorService(doctorRepository);
    }
        @Test
       public  void createOffice_shouldCallRepositoryAndReturnOffice() {
            Office office = new Office();
            Doctor doctor = new Doctor();
            doctor.setFirstName("John");
            doctor.setLastName("Doe");
            doctor.setSpecialty("Cardiology");
            doctor.setEmail("john.doe@example.com");
            doctorService.createDoctor(doctor);
            office.setDoctor(doctor);

            officeService.createOffice(office);
            assertNotNull(office.getOfficeId());
    }
    @Test
    public void testUpdateOffice() {
       Office office = new Office();
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialty("Cardiology");
        doctor.setEmail("john.doe@example.com");
        doctorService.createDoctor(doctor);
        office.setDoctor(doctor);
        officeService.createOffice(office);
        office.setLocation("Charlotte");
        officeService.updateOffice(office);

       Office  updatedDoctor = officeService.getOfficeById(office.getOfficeId());
        assertEquals("Charlotte",updatedDoctor.getLocation() );
    }
    @Test
    public void testDeleteOffice() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialty("Dermatology");
        doctor.setEmail("john.doe@example.com");
        doctorService.createDoctor(doctor);

        Office office=new Office();
         office.setDoctor(doctor);
         officeService.createOffice(office);
        int id = doctor.getDoctorId();
         doctorService.deleteDoctor(id);
        int id1 = office.getOfficeId();
         officeService.deleteOffice(id1);


        assertNull(officeService.getOfficeById(id1));
    }
    @ParameterizedTest
    @CsvSource({"Main Office", "Branch Office"})
    public void testCreateDoctorWithDifferentSpecialties(String officeName) {
        Office office = new Office();
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialty("Dermatology");
        doctor.setEmail("john.doe@example.com");
        doctorService.createDoctor(doctor);
        office.setDoctor(doctor);
        office.setLocation(officeName);
        Office createdOffice=    officeService.createOffice(office);
        assertNotNull(office.getOfficeId());
        assertEquals(officeName,createdOffice.getLocation());
    }
    @AfterEach
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
