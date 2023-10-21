package com.cclsr.eat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cclsr.eat.entity.AddressBook;
import com.cclsr.eat.mapper.AddressBookMapper;
import com.cclsr.eat.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
