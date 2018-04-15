package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.BeforeClass;

public class ArrayStorageTest extends AbstractArrayStorageTest{


    @BeforeClass
    public void initStorage() {
       storage= new ArrayStorage();
    }



}