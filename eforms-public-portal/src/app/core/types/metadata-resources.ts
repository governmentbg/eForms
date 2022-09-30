export const metadataResources = {
    deadlines : [
        {
          label: "Нормална услуга",
          key: "normalService",
          code: "0006-000083",
          transKey: "METADATA.DEADLINE.NORMAL_SERVICE"
        },
        {
          label: "Бърза услуга",
          key: "fastService",
          code: "0006-000084",
          transKey: "METADATA.DEADLINE.FAST_SERVICE"
        },
        {
          label: "Експресна услуга",
          key: "expressService",
          code: "0006-000085",
          transKey: "METADATA.DEADLINE.EXPRESS_SERVICE"
        },
        {
          label: "Няма нормативно указан срок",
          key: "noDefinitionTermService",
          code: "1006-120001",
          transKey: "METADATA.DEADLINE.NO_TERM"
        }
    ],
    deadlineUnits : [
        {
            label: "Работни дни",
            key: "workDays",
            code: "1006-130001"
        },
        {
            label: "Календарен ден",
            key: "calendarDays",
            code: "1006-130002"
        },
        {
            label: "Работен час",
            key: "workHours",
            code: "1006-130003"
        },
        {
            "label":"Календарен час",
            "key": "calendarHours",
            "code": "1006-130004"
        }
    ],
    supplierStatuses : [
        {
          label: "За обработка",
          value: "draft",
          transKey: "METADATA.SUPPLIER_STATUS.DRAFT"

        },
        {
          label: "Активен",
          value: "active",
          transKey: "METADATA.SUPPLIER_STATUS.ACTIVE"
        },
        {
          label: "Неактивен",
          value: "inactive",
          transKey: "METADATA.SUPPLIER_STATUS.INACTIVE"
        },
        {
          label: "Публикуван",
          value: "published",
          transKey: "METADATA.SUPPLIER_STATUS.PUBLISHED"
        }
    ]
}