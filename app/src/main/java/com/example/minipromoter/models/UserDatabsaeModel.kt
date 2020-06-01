package com.example.minipromoter.models

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.android.parcel.Parcelize


//
// Created by Abdul Basit on 3/8/2020.
//

//user model
@Parcelize
@Entity(tableName = "users")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0,
    val subscriptionDate: Long = System.currentTimeMillis(),
    val username: String? = null,
    val companyId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val isActivated: Boolean? = false,
    val isApproved: Boolean? = false,
    val createdOn: Long? = System.currentTimeMillis(),
    val createdBy: String? = null,
    val updatedOn: Long? = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val roleId: Long? = 0
) : Parcelable

//product model
@Entity(tableName = "products")
@Parcelize
data class ProductModel constructor(
    @PrimaryKey(autoGenerate = true)
    var productId: Long = 0,
    val productName: String = "",
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val updatedOn: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val isDeleted: Boolean = false
) : Parcelable

// campaign table and we have 1-m relationships between products and campaign
@Parcelize
@Entity(
    tableName = "campaigns_table",
    foreignKeys = [ForeignKey(
        entity = ProductModel::class,
        parentColumns = arrayOf("productId"),
        childColumns = arrayOf("productId"),
        onDelete = CASCADE
    )]
)
data class Campaign(
    @PrimaryKey(autoGenerate = true)
    var campaignId: Long = 0,
    var campaignType: String? = null,
    var campaignTittle: String? = null,
    var campaignMessage: String? = null,
    val startOn: Long = System.currentTimeMillis(),
    val endOn: Long = System.currentTimeMillis(),
    var expiryAutoMessageReply: String? = null,
    var invalidResponseAutoReply: String? = null,
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val updatedOn: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    var sendAutoReply: Boolean = false,
    var batchSize: Int = 0,
    var priority: Int = 0,
    val productId: Long
) : Parcelable

//keywords table       we have 1-m relationship between campaign and keywords
@Parcelize
@Entity(
    tableName = "keywords_table",
    foreignKeys = [ForeignKey(
        entity = Campaign::class,
        parentColumns = arrayOf("campaignId"),
        childColumns = arrayOf("campaignId"),
        onDelete = CASCADE
    )]
)
data class Keywords(
    @PrimaryKey(autoGenerate = true)
    val keywordId: Long = 0,
    var name: String? = null,
    var type: String? = null,
    var status: String? = null,
    var autoSubscribe: Boolean = false,
    var isEnabled: Boolean = false,
    var description: String? = null,
    var inviteMessage: String? = null,
    var rejectionMessage: String? = null,
    val updatedOn: Long = System.currentTimeMillis(),
    val createdOn: Long = System.currentTimeMillis(),
    val suspend: Boolean = false,
    val isOption: Boolean = false,
    var count: Int = 0,
    val campaignId: Long
) : Parcelable


//campaign message table        we have 1-m relationship between campaign and campaign message
@Parcelize
@Entity(
    tableName = "campaign_messages",
    foreignKeys = [ForeignKey(
        entity = Campaign::class,
        parentColumns = arrayOf("campaignId"),
        childColumns = arrayOf("campaignId"),
        onDelete = CASCADE
    )]
)

data class CampaignMessages(
    @PrimaryKey(autoGenerate = true)
    val messageId: Long = 0,
    var broadcastRequestId: String? = null,
    var messageType: String? = null,
    var message: String? = null,
    var sendTo: String? = null,
    var isConversationMessage: Boolean = false,
    var messageDeliverStatus: Boolean = false,
    val updatedOn: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val createdOn: Long = System.currentTimeMillis(),
    val pendingFlag: Boolean = false,
    //  val userId: Long,
    val disposition: String? = null,
    var priority: Int = 1,
    val isRead: Boolean = false,
    val campaignId: Long
) : Parcelable


//products subscribers        we have m-m relationship between products and users
@Entity(
    tableName = "subscribers_products_table",
    foreignKeys = [ForeignKey(
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("parentUserId"),
        entity = UserModel::class
    ), ForeignKey(
        parentColumns = arrayOf("productId"),
        childColumns = arrayOf("parentProductId"),
        entity = ProductModel::class
    )]

)
data class SubscribersProductsCrossRef(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val parentUserId: Long,
    val parentProductId: Long,
    var isActive: Boolean = true,
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val updatedOn: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val isDeleted: Boolean = false
)

//user message table        1-m relationship between user and message
@Parcelize
@Entity(
    tableName = "user_message",
    foreignKeys = [ForeignKey(
        entity = UserModel::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = CASCADE
    )]
)
data class UserMessage(
    @PrimaryKey(autoGenerate = true)
    val messageId: Long = 0,
    var broadcastRequestId: String? = null,
    var messageType: String? = null,
    var message: String? = null,
    var sendTo: String? = null,
    var isConversationMessage: Boolean = true,
    var isSuccessfullySend: Boolean = false,
    var messageDeliverStatus: Boolean = false,
    val createdOn: Long = System.currentTimeMillis(),
    val pendingFlag: Boolean = false,
    val isIncomingMessage: Boolean = true,
    val disposition: String? = null,
    var priority: Int = 1,
    val isRead: Boolean = false,
    val userId: Long
) : Parcelable


@Parcelize
@Entity(
    tableName = "pending_outgoing_user_message",
    foreignKeys = [ForeignKey(
        entity = UserModel::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("outgoingMessageUserId"),
        onDelete = CASCADE
    )]
)
data class PendingUserOutgoingMessages(
    @PrimaryKey(autoGenerate = true)
    val outgoingMessageId: Long = 0,
    var messageType: String? = null,
    var message: String? = null,
    var sendTo: String? = null,
    var isConversationMessage: Boolean = true,
    var isSuccessfullySend: Boolean = false,
    var messageDeliverStatus: Boolean = false,
    val createdOn: Long = System.currentTimeMillis(),
    val pendingFlag: Boolean = false,
    var priority: Int = 1,
    val isRead: Boolean = false,
    val outgoingMessageUserId: Long
) : Parcelable


/*

@Entity(tableName = "processedMessages")
@Parcelize
data class ProcessedMessages constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val text: String? = null,
    val senderNumber: String? = null,
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null
) : Parcelable

@Entity(tableName = "messageTypes")
@Parcelize
data class MessageTypes constructor(
    @PrimaryKey(autoGenerate = true)
    var typeName: String = "",
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null
) : Parcelable

@Entity(tableName = "incomingMessage")
@Parcelize
data class IncomingMessage constructor(
    @PrimaryKey(autoGenerate = true)
    val messageId: Long = 0,
    var text: String? = null,
    var senderNumber: String? = null,
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val errorFlag: String? = null
) : Parcelable

@Entity(tableName = "productAttributes")
@Parcelize
data class ProductAttributes constructor(
    @PrimaryKey(autoGenerate = true)
    val attributeId: Long = 0,
    var name: String? = null,
    var value: String? = null,
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val updatedOn: Long = System.currentTimeMillis(),
    val updateBy: String? = null,
    val productId: Long
) : Parcelable

@Entity(tableName = "product_and_product_association_table")
data class ProductAndProductAssociationTable(
    @Embedded val owner: ProductModel,
    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val productAttributes: List<ProductAttributes>
)

@Entity(tableName = "jobs_table")
data class Jobs(
    @PrimaryKey(autoGenerate = true)
    val jobId: Long = 0,
    var name: String? = null,
    var isEnabled: Boolean = true,
    var isProcessig: Boolean = false,
    val startAt: Long = System.currentTimeMillis(),
    val lastRunAt: Long = System.currentTimeMillis(),
    val interval: Long = System.currentTimeMillis(),
    val errorFlag: Long = System.currentTimeMillis(),
    val warnigThreashold: Long = System.currentTimeMillis()
)

@Entity(tableName = "subscribers_products_table", primaryKeys = ["userId", "productId"])
data class SubscribersProductsCrossRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val productId: Long,
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val updatedOn: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val isDeleted: Boolean = false
)

@Entity(tableName = "campiagn_types_table")
data class CampaignTypes(
    @PrimaryKey(autoGenerate = true)
    val campaignTypeName: String? = "",
    var name: String? = null,
    var isEnabled: Boolean = true,
    var isProcessing: Boolean = false,
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val updatedOn: Long = System.currentTimeMillis(),
    val updatedBy: String? = null
)

*/


/*
@Entity(tableName = "keywords_table")
data class keywords(
    @PrimaryKey(autoGenerate = true)
    val keywordId: Long = 0,
    var name: String? = null,
    var type: String? = null,
    var status: String? = null,
    var autSubscribe: Boolean = false,
    var isEnabled: Boolean = false,
    var description: String? = null,
    var inviteMessage: String? = null,
    var rejectionMessage: String? = null,
    val updatedOn: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val suspend: Boolean = false,
    val campaignId: Long
)

@Entity(tableName = "campign_and_keywords_cross_ref")
data class CampaignAndKeywordCrossRef(
    @Embedded val owner: Campaign,
    @Relation(
        parentColumn = "campaignId",
        entityColumn = "campaignId"
    )
    val productAttributes: List<keywords>
)


@Entity(tableName = "campaign_message")
data class CampaignMessage(
    @PrimaryKey(autoGenerate = true)
    val messageId: Long = 0,
    var broadcastRequestId: String? = null,
    var messageType: String? = null,
    var message: String? = null,
    var sendTo: String? = null,
    var isConversationMessage: Boolean = false,
    var messageDeliverStatus: Boolean = false,
    val updatedOn: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val pendingFlag: Boolean = false,
    val userId: Long,
    val disposition: String? = null,
    var priority: Int,
    val isRead: Boolean = false,
    val campaignId: Long
)

@Entity(tableName = "campign_and_campaignmessage_cross_ref")
data class CampaignAndCampaignMessageCrossRef(
    @Embedded val owner: Campaign,
    @Relation(
        parentColumn = "campaignId",
        entityColumn = "campaignId"
    )
    val productAttributes: List<CampaignMessage>
)*/

/*

@Entity(
    tableName = "campaigns",
    foreignKeys = [ForeignKey(
        entity = ProductModel::class,
        parentColumns = arrayOf("productId"),
        childColumns = arrayOf("productId"),
        onDelete = CASCADE
    )]
)
@Parcelize
class Campaign(
    @PrimaryKey(autoGenerate = true)
    val campaignId: Long = 0,
    val campaignName: String,
    val campaignMessage: String,
    val campaignCreationDate: Long = System.currentTimeMillis(),
    val productId: Long
) : Parcelable
*/
/*

@Entity(
    tableName = "campaign_messages",
    foreignKeys = [ForeignKey(
        entity = Campaign::class,
        parentColumns = arrayOf("campaignId"),
        childColumns = arrayOf("campaignId"),
        onDelete = CASCADE
    )]
)
data class CampaignMessages(
    @PrimaryKey(autoGenerate = true)
    val campaignMessageId: Long = 0,
    val campaignMessage: String = "",
    val campaignMessageCreationDate: Long = System.currentTimeMillis(),
    val campaignId: Long
)

@Entity(tableName = "product_subscribers", primaryKeys = ["userId", "productId"])
data class ProductSubscribers(
    val userId: Long,
    val productId: Long
)


*/
