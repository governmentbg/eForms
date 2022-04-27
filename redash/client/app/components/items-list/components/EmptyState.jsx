import React from "react";
import BigMessage from "@/components/BigMessage";
import { t } from "@/locales/config.jsx";

// Default "list empty" message for list pages
export default function EmptyState(props) {
  return (
    <div className="text-center">
      <BigMessage icon="fa-search" message={t("Sorry, we couldn't find anything.")} {...props} />
    </div>
  );
}
