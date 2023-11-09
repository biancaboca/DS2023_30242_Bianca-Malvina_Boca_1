package ro.tuc.ds2020.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PersonTabel {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="IdPerson")
    @Type(type = "uuid-binary")
    private UUID id;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Device> deviceList;

    public PersonTabel(UUID id)
    {
        this.id=id;
    }


}


