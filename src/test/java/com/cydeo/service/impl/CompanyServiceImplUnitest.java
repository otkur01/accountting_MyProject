//package com.cydeo.service.impl;
//
//
//import com.cydeo.dto.CompanyDto;
//import com.cydeo.entity.Company;
//import com.cydeo.enums.CompanyStatus;
//import com.cydeo.repository.CompanyRepository;
//import com.cydeo.util.MapperUtil;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//import static org.assertj.core.api.ThrowableAssert.catchThrowable;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//class CompanyServiceImplUnitTest {
//
//    @Mock
//    private CompanyRepository companyRepository;
//
//    @Mock
//    private MapperUtil mapperUtil;
//
//    @InjectMocks
//    private CompanyServiceImpl companyService;
//    @Test
//    void should_throw_exception_when_company_not_found(){
//        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Throwable throwable = catchThrowable(()-> companyService.findById(1L));
//        assertThat(throwable).isInstanceOf(RuntimeException.class);
//
//    }
//
//
//
//
//    @Test
//    void should_get_company_when_company_found(){
//        Company company = new Company();
//        company.setId(1L);
//        company.setTitle("Test Company");
//        company.setPhone("+1 (356) 258-3544");
//        CompanyDto expectedCompanyDto = new CompanyDto();
//        expectedCompanyDto.setId(1L);
//        expectedCompanyDto.setTitle("Test Company");
//
//        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
//        when(mapperUtil.convert(any(Company.class), any(CompanyDto.class))).thenReturn(expectedCompanyDto);
//
//        CompanyDto companyDto = companyService.findById(1L);
//        assertThat(companyDto).isNotNull();
//        assertThat(companyDto).isInstanceOf(CompanyDto.class);
//        assertThat(companyDto.getId()).isEqualTo(1L);
//        assertThat(companyDto.getTitle()).isEqualTo("Test Company");
//
//    }
//
//
//    @Test
//    void should_return_list_of_company(){
//        Company company1 = new Company();
//        company1.setId(1L);
//        company1.setTitle("Company One");
//
//        Company company2 = new Company();
//        company2.setId(2L);
//        company2.setTitle("Company Two");
//
//        List<Company> listOfCompanies = Arrays.asList(company1, company2);
//
//        CompanyDto companyDto1 = new CompanyDto();
//        companyDto1.setId(1L);
//        companyDto1.setTitle("Company One");
//
//        CompanyDto companyDto2 = new CompanyDto();
//        companyDto2.setId(2L);
//        companyDto2.setTitle("Company Two");
//
//        when(companyRepository.findAll()).thenReturn(listOfCompanies);
//        when(mapperUtil.convert(any(Company.class), any(CompanyDto.class)))
//                .thenReturn(companyDto1)
//                .thenReturn(companyDto2);
//
//        List<CompanyDto> companyDtos = companyService.listAllCompanies();
//
//
//        assertThat(companyDtos).isNotNull();
//        assertThat(companyDtos).hasSize(2);
//        assertThat(companyDtos.get(0).getId()).isEqualTo(1L);
//        assertThat(companyDtos.get(0).getTitle()).isEqualTo("Company One");
//        assertThat(companyDtos.get(1).getId()).isEqualTo(2L);
//        assertThat(companyDtos.get(1).getTitle()).isEqualTo("Company Two");
//
//    }
//
//    @Test
//    void should_return_other_companies(){
//        Company company1 = new Company();
//        company1.setId(2L);
//        company1.setTitle("Company two");
//        List<Company> listOfCompanies = Arrays.asList(company1);
//        CompanyDto companyDto1 = new CompanyDto();
//        companyDto1.setId(2L);
//        companyDto1.setTitle("Company two");
//        when(companyRepository.findAllByIdIsNotOrderByCompanyStatus(1L)).thenReturn(listOfCompanies);
//        when(mapperUtil.convert(any(Company.class), any(CompanyDto.class)))
//                .thenReturn(companyDto1);
//        List<CompanyDto> companyDtos = companyService.getAllCompaniesByIdNotEqualOrderedByStatus(1L);
//        assertThat(companyDtos).isNotNull();
//        assertThat(companyDtos).hasSize(1);
//        assertThat(companyDtos.get(0).getId()).isEqualTo(2L);
//        assertThat(companyDtos.get(0).getTitle()).isEqualTo("Company two");
//
//
//    }
//
//    @Test
//    void should_save_company(){
//        Company company1 = new Company();
//        company1.setId(1L);
//        company1.setTitle("Company One");
//
//
//        CompanyDto companyDto1 = new CompanyDto();
//        companyDto1.setId(1L);
//        companyDto1.setTitle("Company One");
//
//
//        when(companyRepository.save(company1)).thenReturn(company1);
//        when(mapperUtil.convert(any(Company.class), any(CompanyDto.class))).thenReturn(companyDto1);
//        when(mapperUtil.convert(any(CompanyDto.class), any(Company.class))).thenReturn(company1);
//
//
//        CompanyDto companyDto = companyService.save(companyDto1);
//        assertThat(companyDto).isNotNull();
//        assertThat(companyDto).isInstanceOf(CompanyDto.class);
//        assertThat(companyDto.getId()).isEqualTo(1L);
//        assertThat(companyDto.getTitle()).isEqualTo("Company One");
//        assertThat(companyDto.getCompanyStatus()).isEqualTo(CompanyStatus.PASSIVE);
//
//    }
//
//    @Test
//    void should_update_company(){
//        Company company1 = new Company();
//        company1.setId(1L);
//        company1.setTitle("Company One");
//
//
//        CompanyDto companyDto1 = new CompanyDto();
//        companyDto1.setId(1L);
//        companyDto1.setTitle("Company One");
//
//        when(companyRepository.save(company1)).thenReturn(company1);
//        when(mapperUtil.convert(any(Company.class), any(CompanyDto.class))).thenReturn(companyDto1);
//        when(mapperUtil.convert(any(CompanyDto.class), any(Company.class))).thenReturn(company1);
//
//        CompanyDto companyDto = companyService.update(companyDto1);
//        assertThat(companyDto).isNotNull();
//        assertThat(companyDto).isInstanceOf(CompanyDto.class);
//        assertThat(companyDto.getId()).isEqualTo(1L);
//        assertThat(companyDto.getTitle()).isEqualTo("Company One");
//        assertThat(companyDto.getCompanyStatus()).isEqualTo(CompanyStatus.ACTIVE);
//
//
//
//    }
//
//
//    @Test
//    void should_activate_company(){
//        Company company1 = new Company();
//        company1.setId(1L);
//        company1.setTitle("Company One");
//        company1.setCompanyStatus(CompanyStatus.PASSIVE);
//
//
//        when(companyRepository.findById(1L)).thenReturn(Optional.of(company1));
//
//        companyService.activeCompanyStatus(1L);
//
//        assertThat(company1.getCompanyStatus()).isEqualTo(CompanyStatus.ACTIVE);
//
//
//    }
//    @Test
//    void should_deactivate_company(){
//        Company company1 = new Company();
//        company1.setId(1L);
//        company1.setTitle("Company One");
//        company1.setCompanyStatus(CompanyStatus.ACTIVE);
//
//
//        when(companyRepository.findById(1L)).thenReturn(Optional.of(company1));
//
//        companyService.deactivateCompanyStatus(1L);
//
//        assertThat(company1.getCompanyStatus()).isEqualTo(CompanyStatus.PASSIVE);
//
//    }
//
//    @Test
//    void should_return_true_if_company_not_found_with_that_title(){
//        Company company1 = new Company();
//        company1.setId(1L);
//        company1.setTitle("Company One");
//
//
//        Company company2 = new Company();
//        company2.setId(2L);
//        company2.setTitle("Company Two");
//
//        when(companyRepository.findByTitle("Company One")).thenReturn(company1);
//        when(companyRepository.findByTitle("Company Two")).thenReturn(company2);
//        when(companyRepository.findByTitle("Company")).thenReturn(null);
//
//        assertThat(companyService.isTitleUnique("Company")).isTrue();
//        assertThat(companyService.isTitleUnique("Company One")).isFalse();
//        assertThat(companyService.isTitleUnique("Company Two")).isFalse();
//
//
//    }
//
//
//
//}