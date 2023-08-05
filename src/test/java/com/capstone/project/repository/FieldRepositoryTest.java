package com.capstone.project.repository;

import com.capstone.project.model.Field;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FieldRepositoryTest {

    @Autowired
    private FieldRepository fieldRepository;

    @Test
    public void testFindFieldsByType_Id() {
        int typeIdToFind = 1;
        List<Field> fields = fieldRepository.findFieldsByType_Id(typeIdToFind);

        Assertions.assertThat(fields.size()).isGreaterThan(0);
    }
}
