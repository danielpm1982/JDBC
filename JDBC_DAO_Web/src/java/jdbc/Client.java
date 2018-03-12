package jdbc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Client {
    private int id;
    private String name;
    private BigDecimal salary;
    private LocalDate birthDate;
    private LocalDateTime registeredIn;
    private int departmentNo;
    public Client(String name, BigDecimal salary, LocalDate birthDate, LocalDateTime registeredIn, int departmentNo) {
        this.id=-1; //real id defined at the dbms, not here.
        this.name = name;
        this.salary = salary;
        this.birthDate = birthDate;
        this.registeredIn = registeredIn;
        this.departmentNo = departmentNo;
    }
    public Client() {
        this(null,null,null,null,-1);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getSalary() {
        return salary;
    }
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public LocalDateTime getRegisteredIn() {
        return registeredIn;
    }
    public void setRegisteredIn(LocalDateTime registeredIn) {
        this.registeredIn = registeredIn;
    }
    public int getDepartmentNo() {
        return departmentNo;
    }
    public void setDepartmentNo(int departmentNo) {
        this.departmentNo = departmentNo;
    }
    public String getClientWithoutId() {
        return "name: "+name+" salary: "+salary+" birthDate: "+birthDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))+" registeredIn: "+registeredIn.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))+" departmentNo: "+departmentNo;
    }
    @Override
    public String toString() {
        return "id: "+id+" name: "+name+" salary: "+salary+" birthDate: "+birthDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))+" registeredIn: "+registeredIn.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))+" departmentNo: "+departmentNo;
    }
}
