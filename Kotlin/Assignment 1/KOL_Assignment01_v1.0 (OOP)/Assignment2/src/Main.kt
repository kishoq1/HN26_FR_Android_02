sealed class ContactInformation {
    abstract val info: String
}

data class PhoneNumber(override val info: String) : ContactInformation()
data class EmailAddress(override val info: String) : ContactInformation()
data class SocialMediaAccount(
    override val info: String,
    val platform: String
) : ContactInformation()

class Contact<out T : ContactInformation>(
    val name: String,
    val list: List<T>
) {
    fun displayContactDetails() {
        println("Tên liên hệ: $name")
        list.forEach { item ->
            when (item) {
                is PhoneNumber -> println(" Điện thoại: ${item.info}")
                is EmailAddress -> println("Email: ${item.info}")
                is SocialMediaAccount -> println("Mạng xã hội (${item.platform}): ${item.info}")
            }
        }
        println("-----------------------------")
    }
}

private val sampleDirectory = object {
    val phoneContact = Contact(
        name = "Nguyễn Văn A",
        list = listOf(PhoneNumber("0987654321"), PhoneNumber("0123456789"))
    )

    val emailContact = Contact(
        name = "Trần Thị B",
        list = listOf(EmailAddress("tran.b@example.com"))
    )

    val socialContact = Contact(
        name = "Lê Văn C",
        list = listOf(SocialMediaAccount("le_c_dev", "GitHub"), SocialMediaAccount("le.c.official", "LinkedIn"))
    )
}

object ContactManager {
    private val contacts = mutableListOf<Contact<ContactInformation>>()

    init {
        contacts.add(sampleDirectory.phoneContact)
        contacts.add(sampleDirectory.emailContact)
        contacts.add(sampleDirectory.socialContact)
    }

    fun addContact(contact: Contact<ContactInformation>) {
        contacts.add(contact)
        println("Đã thêm liên hệ: ${contact.name}")
    }

    fun displayAllContacts() {
        println("\n========== DANH BẠ ==========")
        if (contacts.isEmpty()) {
            println("Danh bạ trống.")
        } else {
            contacts.forEach { it.displayContactDetails() }
        }
        println("=============================\n")
    }
}

fun main() {
    println("Hiển thị danh bạ ban đầu từ hệ thống:")
    ContactManager.displayAllContacts()

    val newSocialContact = Contact(
        name = "Phạm D",
        list = listOf(SocialMediaAccount("pham_d_gaming", "Discord"))
    )

    ContactManager.addContact(newSocialContact)

    println("Hiển thị danh bạ sau khi cập nhật:")
    ContactManager.displayAllContacts()
}