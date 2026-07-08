package com.example.assignment7.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignment7.data.model.Contact
import com.example.assignment7.viewmodel.ContactViewModel

@Composable
fun ContactScreen(viewModel: ContactViewModel) {
    val contactList by viewModel.contacts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSortAsc by viewModel.isSortAscending.collectAsState()

    ContactContent(
        contactList = contactList,
        searchQuery = searchQuery,
        isSortAsc = isSortAsc,
        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
        onToggleSort = { viewModel.toggleSortOrder() },
        onAddContact = { name, phone -> viewModel.addContact(name, phone) },
        onUpdateContact = { contact -> viewModel.updateContact(contact) },
        onDeleteContact = { contact -> viewModel.deleteContact(contact) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactContent(
    contactList: List<Contact>,
    searchQuery: String,
    isSortAsc: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onToggleSort: () -> Unit,
    onAddContact: (String, String) -> Unit,
    onUpdateContact: (Contact) -> Unit,
    onDeleteContact: (Contact) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var editingContact by remember { mutableStateOf<Contact?>(null) }

    val phoneRegex = "^0\\d{9}$".toRegex()

    val isNameDuplicate = contactList.any {
        it.name.trim().equals(name.trim(), ignoreCase = true) && it.id != editingContact?.id
    }
    val isPhoneDuplicate = contactList.any {
        it.phoneNumber == phoneNumber && it.id != editingContact?.id
    }

    val isPhoneInvalidFormat = phoneNumber.isNotEmpty() && !phoneNumber.matches(phoneRegex)
    val isPhoneError = isPhoneInvalidFormat || isPhoneDuplicate
    val isNameError = isNameDuplicate

    val isFormValid = name.isNotBlank() && !isNameError &&
            phoneNumber.matches(phoneRegex) && !isPhoneDuplicate

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Danh bạ (Kotlin Flow)", maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                actions = {
                    TextButton(onClick = onToggleSort) {
                        Text(if (isSortAsc) "Sắp xếp: A-Z" else "Sắp xếp: Z-A")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ô TÌM KIẾM
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                label = { Text("Tìm kiếm theo tên hoặc số điện thoại") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // FORM NHẬP LIỆU
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (editingContact == null) "Thêm danh bạ mới" else "Chỉnh sửa danh bạ",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Họ và tên") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = isNameError,
                        supportingText = {
                            if (isNameError) {
                                Text("Tên này đã tồn tại trong danh bạ!", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() }) phoneNumber = it
                        },
                        label = { Text("Số điện thoại") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = isPhoneError,
                        supportingText = {
                            if (isPhoneInvalidFormat) {
                                Text("Vui lòng nhập đúng 10 số và bắt đầu bằng số 0", color = MaterialTheme.colorScheme.error)
                            } else if (isPhoneDuplicate) {
                                Text("Số điện thoại này đã được lưu cho người khác!", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (editingContact != null) {
                            TextButton(onClick = {
                                editingContact = null
                                name = ""
                                phoneNumber = ""
                            }) { Text("Hủy") }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Button(
                            onClick = {
                                if (isFormValid) {
                                    val currentEdit = editingContact
                                    val cleanName = name.trim()

                                    if (currentEdit == null) {
                                        onAddContact(cleanName, phoneNumber)
                                    } else {
                                        onUpdateContact(currentEdit.copy(name = cleanName, phoneNumber = phoneNumber))
                                        editingContact = null
                                    }
                                    name = ""
                                    phoneNumber = ""
                                }
                            },
                            enabled = isFormValid
                        ) {
                            Text(if (editingContact == null) "Thêm" else "Cập nhật")
                        }
                    }
                }
            }

            // DANH SÁCH LIÊN HỆ
            Text(
                text = "Danh sách liên hệ (${contactList.size})",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contactList, key = { it.id }) { contact ->
                    ContactItem(
                        contact = contact,
                        onEditClick = {
                            editingContact = contact
                            name = contact.name
                            phoneNumber = contact.phoneNumber
                        },
                        onDeleteClick = { onDeleteContact(contact) }
                    )
                }
            }
        }
    }
}

@Composable
fun ContactItem(
    contact: Contact,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = contact.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row {
                TextButton(onClick = onEditClick) { Text("Sửa") }
                TextButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Xóa") }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactContentPreview() {
    val mockContacts = listOf(
        Contact(id = 1, name = "Nguyễn Văn A", phoneNumber = "0901234567"),
        Contact(id = 2, name = "Trần Thị B", phoneNumber = "0987654321")
    )

    MaterialTheme {
        ContactContent(
            contactList = mockContacts,
            searchQuery = "",
            isSortAsc = true,
            onSearchQueryChanged = {},
            onToggleSort = {},
            onAddContact = { _, _ -> },
            onUpdateContact = {},
            onDeleteContact = {}
        )
    }
}