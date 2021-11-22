package at.tecs.smartpos_demo.main.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.main.dialog.TransactionSettingsDialog;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private ArrayList<TransactionEntity> transactions;
    private final Context context;

    public TransactionAdapter(ArrayList<TransactionEntity> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton transSettingsButton;
        private final Button transSendButton;
        private final ImageButton expandButton;
        private final TextView messageTypeText;

        private final TextInputEditText amountEditText;
        private final TextInputEditText currencyEditText;
        private final TextInputEditText transSourceIDEdit;
        private final TextInputEditText cardNumEditText;
        private final TextInputEditText cvc2EditText;
        private final TextInputEditText receiptNumEditText;
        private final TextInputEditText paymentReasonEditText;
        private final TextInputEditText transPlaceEditText;
        private final TextInputEditText authNumberEditText;
        private final TextInputEditText originIndicatorEditText;
        private final TextInputEditText passwordEditText;
        private final TextInputEditText ecrDataEditText;
        private final TextInputEditText languageCodeEditText;
        private final TextInputEditText receiptLayoutEditText;
        private final TextInputEditText destinationCurrencyEditText;
        private final TextInputEditText txOriginEditText;
        private final TextInputEditText personalIDEditText;

        private final TextInputLayout amountEditLayout;
        private final TextInputLayout currencyEditLayout;
        private final TextInputLayout transSourceIDEditLayout;
        private final TextInputLayout cardNumEditLayout;
        private final TextInputLayout cvc2EditLayout;
        private final TextInputLayout receiptNumEditLayout;
        private final TextInputLayout paymentReasonEditLayout;
        private final TextInputLayout transPlaceEditLayout;
        private final TextInputLayout authNumberEditLayout;
        private final TextInputLayout originIndicatorEditLayout;
        private final TextInputLayout passwordEditLayout;
        private final TextInputLayout ecrDataEditLayout;
        private final TextInputLayout receiptLayoutEditLayout;
        private final TextInputLayout destinationCurrencyEditLayout;
        private final TextInputLayout txOriginEditLayout;
        private final TextInputLayout personalIDEditLayout;
        private final TextInputLayout languageCodeEditLayout;

        public ViewHolder(View view) {
            super(view);

            transSendButton = view.findViewById(R.id.transSendButton);
            expandButton = view.findViewById(R.id.expandButton);
            transSettingsButton = view.findViewById(R.id.transSettingsButton);

            messageTypeText = view.findViewById(R.id.messageTypeEdit);
            amountEditText = view.findViewById(R.id.amountEditText);
            currencyEditText = view.findViewById(R.id.currencyEditText);
            transSourceIDEdit = view.findViewById(R.id.transSourceIDEdit);
            cardNumEditText = view.findViewById(R.id.cardNumEditText);
            cvc2EditText = view.findViewById(R.id.cvc2EditText);
            receiptNumEditText = view.findViewById(R.id.receiptNumEditText);
            paymentReasonEditText = view.findViewById(R.id.paymentReasonEditText);
            transPlaceEditText = view.findViewById(R.id.transPlaceEditText);
            authNumberEditText = view.findViewById(R.id.authNumberEditText);
            originIndicatorEditText = view.findViewById(R.id.originIndicatorEditText);
            passwordEditText = view.findViewById(R.id.passwordEditText);
            ecrDataEditText = view.findViewById(R.id.ecrDataEditText);
            languageCodeEditText = view.findViewById(R.id.languageCodeEditText);
            receiptLayoutEditText = view.findViewById(R.id.receiptLayoutEditText);
            destinationCurrencyEditText = view.findViewById(R.id.destinationCurrencyEditText);
            txOriginEditText = view.findViewById(R.id.txOriginEditText);
            personalIDEditText = view.findViewById(R.id.personalIDEditText);

            amountEditLayout = view.findViewById(R.id.amountEditLayout);
            currencyEditLayout = view.findViewById(R.id.currencyEditLayout);
            transSourceIDEditLayout = view.findViewById(R.id.transSourceIDEditLayout);
            cardNumEditLayout = view.findViewById(R.id.cardNumEditLayout);
            cvc2EditLayout = view.findViewById(R.id.cvc2EditLayout);
            receiptNumEditLayout = view.findViewById(R.id.receiptNumEditLayout);
            paymentReasonEditLayout = view.findViewById(R.id.paymentReasonEditLayout);
            transPlaceEditLayout = view.findViewById(R.id.transPlaceEditLayout);
            authNumberEditLayout = view.findViewById(R.id.authNumberEditLayout);
            originIndicatorEditLayout = view.findViewById(R.id.originIndicatorEditLayout);
            passwordEditLayout = view.findViewById(R.id.passwordEditLayout);
            ecrDataEditLayout = view.findViewById(R.id.ecrDataEditLayout);
            languageCodeEditLayout = view.findViewById(R.id.languageCodeEditLayout);
            receiptLayoutEditLayout = view.findViewById(R.id.receiptLayoutEditLayout);
            destinationCurrencyEditLayout = view.findViewById(R.id.destinationCurrencyEditLayout);
            txOriginEditLayout = view.findViewById(R.id.txOriginEditLayout);
            personalIDEditLayout = view.findViewById(R.id.personalIDEditLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transactions_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final TransactionEntity transaction = transactions.get(i);

        showTransaction(transaction, viewHolder);

        final int index = i;

        viewHolder.transSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransactionSettings(getTransaction(transaction, viewHolder), context);
            }
        });

        viewHolder.transSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Repository repository = Repository.getInstance(context);
                repository.updateTransaction(transaction);

                int currentPossition = transactions.indexOf(transaction);
                transactions.remove(currentPossition);
                transactions.add(0, transaction);

                for(int i = 0; i < transactions.size(); i++) {
                    notifyItemChanged(i);
                }
            }
        });

        viewHolder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(transaction.expanded) {
                    ((ImageButton) v).setImageResource(R.drawable.outline_expand_less_white_24dp);
                } else {
                    ((ImageButton) v).setImageResource(R.drawable.outline_expand_more_white_24dp);
                }

                transaction.expanded = !transaction.expanded;

                notifyItemChanged(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    private void showTransaction(TransactionEntity transaction,final ViewHolder viewHolder) {

        if(transaction.msgType != null) {
            viewHolder.messageTypeText.setText(transaction.msgType);
        }

        if(transaction.name != null) {
            viewHolder.transSendButton.setText(transaction.name);
        }

        if(transaction.expanded) {
            if (transaction.amount != null) {
                viewHolder.amountEditText.setText(transaction.amount);
                if (transaction.amountVisibility != null && !transaction.amountVisibility) {
                    viewHolder.amountEditLayout.setVisibility(View.GONE);
                    viewHolder.currencyEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.amountEditLayout.setVisibility(View.VISIBLE);
                    viewHolder.currencyEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.currency != null) {
                viewHolder.currencyEditText.setText(transaction.currency);
                if (transaction.currencyVisibility != null && !transaction.currencyVisibility) {
                    viewHolder.currencyEditText.setEnabled(false);
                } else {
                    viewHolder.currencyEditLayout.setVisibility(View.VISIBLE);
                    viewHolder.currencyEditText.setEnabled(true);
                }
            }

            if (transaction.sourceID != null) {
                viewHolder.transSourceIDEdit.setText(transaction.sourceID);
                if (transaction.sourceIDVisibility != null && !transaction.sourceIDVisibility) {
                    viewHolder.transSourceIDEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.transSourceIDEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.cardNum != null) {
                viewHolder.cardNumEditText.setText(transaction.cardNum);
                if (transaction.cardNumVisibility != null && !transaction.cardNumVisibility) {
                    viewHolder.cardNumEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.cardNumEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.cvc2 != null) {
                viewHolder.cvc2EditText.setText(transaction.cvc2);
                if (transaction.cvc2Visibility != null && !transaction.cvc2Visibility) {
                    viewHolder.cvc2EditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.cvc2EditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.receiptNum != null) {
                viewHolder.receiptNumEditText.setText(transaction.receiptNum);
                if (transaction.receiptNumVisibility != null && !transaction.receiptNumVisibility) {
                    viewHolder.receiptNumEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.receiptNumEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.paymentReason != null) {
                viewHolder.paymentReasonEditText.setText(transaction.paymentReason);
                if (transaction.paymentReasonVisibility != null && !transaction.paymentReasonVisibility) {
                    viewHolder.paymentReasonEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.paymentReasonEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.transPlace != null) {
                viewHolder.transPlaceEditText.setText(transaction.transPlace);
                if (transaction.transPlaceVisibility != null && !transaction.transPlaceVisibility) {
                    viewHolder.transPlaceEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.transPlaceEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.authorNum != null) {
                viewHolder.authNumberEditText.setText(transaction.transPlace);
                if (transaction.authorNumVisibility != null && !transaction.authorNumVisibility) {
                    viewHolder.authNumberEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.authNumberEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.originInd != null) {
                viewHolder.originIndicatorEditText.setText(transaction.originInd);
                if (transaction.originIndVisibility != null && !transaction.originIndVisibility) {
                    viewHolder.originIndicatorEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.originIndicatorEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.password != null) {
                viewHolder.passwordEditText.setText(transaction.password);
                if (transaction.passwordVisibility != null && !transaction.passwordVisibility) {
                    viewHolder.passwordEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.passwordEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.userdata != null) {
                viewHolder.ecrDataEditText.setText(transaction.userdata);
                if (transaction.userdataVisibility != null && !transaction.userdataVisibility) {
                    viewHolder.ecrDataEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.ecrDataEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.langCode != null) {
                viewHolder.languageCodeEditText.setText(transaction.langCode);
                if (transaction.langCodeVisibility != null && !transaction.langCodeVisibility) {
                    viewHolder.languageCodeEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.languageCodeEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.receiptLayout != null) {
                viewHolder.receiptLayoutEditText.setText(transaction.receiptLayout);
                if (transaction.receiptLayoutVisibility != null && !transaction.receiptLayoutVisibility) {
                    viewHolder.receiptLayoutEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.receiptLayoutEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.desCurrency != null) {
                viewHolder.destinationCurrencyEditText.setText(transaction.desCurrency);
                if (transaction.desCurrencyVisibility != null && !transaction.desCurrencyVisibility) {
                    viewHolder.destinationCurrencyEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.destinationCurrencyEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.txOrigin != null) {
                viewHolder.txOriginEditText.setText(transaction.txOrigin);
                if (transaction.txOriginVisibility != null && !transaction.txOriginVisibility) {
                    viewHolder.txOriginEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.txOriginEditLayout.setVisibility(View.VISIBLE);
                }
            }

            if (transaction.personalID != null) {
                viewHolder.personalIDEditText.setText(transaction.personalID);
                if (transaction.personalIDVisibility != null && !transaction.personalIDVisibility) {
                    viewHolder.personalIDEditLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.personalIDEditLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            viewHolder.amountEditLayout.setVisibility(View.GONE);
            viewHolder.currencyEditLayout.setVisibility(View.GONE);
            viewHolder.transSourceIDEditLayout.setVisibility(View.GONE);
            viewHolder.cardNumEditLayout.setVisibility(View.GONE);
            viewHolder.cvc2EditLayout.setVisibility(View.GONE);
            viewHolder.receiptNumEditLayout.setVisibility(View.GONE);
            viewHolder.paymentReasonEditLayout.setVisibility(View.GONE);
            viewHolder.transPlaceEditLayout.setVisibility(View.GONE);
            viewHolder.authNumberEditLayout.setVisibility(View.GONE);
            viewHolder.originIndicatorEditLayout.setVisibility(View.GONE);
            viewHolder.passwordEditLayout.setVisibility(View.GONE);
            viewHolder.ecrDataEditLayout.setVisibility(View.GONE);
            viewHolder.languageCodeEditLayout.setVisibility(View.GONE);
            viewHolder.receiptLayoutEditLayout.setVisibility(View.GONE);
            viewHolder.destinationCurrencyEditLayout.setVisibility(View.GONE);
            viewHolder.txOriginEditLayout.setVisibility(View.GONE);
            viewHolder.personalIDEditLayout.setVisibility(View.GONE);
        }
    }

    private void showTransactionSettings(TransactionEntity transactionEntity, final Context context) {
        TransactionSettingsDialog dialog = new TransactionSettingsDialog(context, R.style.CustomDialogTheme);
        dialog.setMode(true);
        dialog.setTransaction(transactionEntity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                transactions = Repository.getInstance(context).getAllTransactions();

                notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private TransactionEntity getTransaction(TransactionEntity transactionEntity, ViewHolder viewHolder) {

        if(viewHolder.amountEditText.getText() != null && !viewHolder.amountEditText.getText().toString().equals(""))
            transactionEntity.amount = viewHolder.amountEditText.getText().toString();
        if(viewHolder.currencyEditText.getText() != null && !viewHolder.currencyEditText.getText().toString().equals(""))
            transactionEntity.currency = viewHolder.currencyEditText.getText().toString();
        if(viewHolder.transSourceIDEdit.getText() != null && !viewHolder.transSourceIDEdit.getText().toString().equals(""))
            transactionEntity.sourceID = viewHolder.transSourceIDEdit.getText().toString();
        if(viewHolder.cardNumEditText.getText() != null && !viewHolder.cardNumEditText.getText().toString().equals(""))
            transactionEntity.cardNum = viewHolder.cardNumEditText.getText().toString();
        if(viewHolder.cvc2EditText.getText() != null && !viewHolder.cvc2EditText.getText().toString().equals(""))
            transactionEntity.cvc2 = viewHolder.cvc2EditText.getText().toString();
        if(viewHolder.receiptNumEditText.getText() != null && !viewHolder.receiptNumEditText.getText().toString().equals(""))
            transactionEntity.receiptNum = viewHolder.receiptNumEditText.getText().toString();
        if(viewHolder.paymentReasonEditText.getText() != null && !viewHolder.paymentReasonEditText.getText().toString().equals(""))
            transactionEntity.paymentReason = viewHolder.paymentReasonEditText.getText().toString();
        if(viewHolder.transPlaceEditText.getText() != null && !viewHolder.transPlaceEditText.getText().toString().equals(""))
            transactionEntity.transPlace = viewHolder.transPlaceEditText.getText().toString();
        if(viewHolder.authNumberEditText.getText() != null && !viewHolder.authNumberEditText.getText().toString().equals(""))
            transactionEntity.authorNum = viewHolder.authNumberEditText.getText().toString();
        if(viewHolder.originIndicatorEditText.getText() != null && !viewHolder.originIndicatorEditText.getText().toString().equals(""))
            transactionEntity.originInd = viewHolder.originIndicatorEditText.getText().toString();
        if(viewHolder.passwordEditText.getText() != null && !viewHolder.passwordEditText.getText().toString().equals(""))
            transactionEntity.password = viewHolder.passwordEditText.getText().toString();
        if(viewHolder.ecrDataEditText.getText() != null && !viewHolder.ecrDataEditText.getText().toString().equals(""))
            transactionEntity.userdata = viewHolder.ecrDataEditText.getText().toString();
        if(viewHolder.languageCodeEditText.getText() != null && !viewHolder.languageCodeEditText.getText().toString().equals(""))
            transactionEntity.langCode = viewHolder.languageCodeEditText.getText().toString();
        if(viewHolder.receiptLayoutEditText.getText() != null && !viewHolder.receiptLayoutEditText.getText().toString().equals(""))
            transactionEntity.receiptLayout = viewHolder.receiptLayoutEditText.getText().toString();
        if(viewHolder.destinationCurrencyEditText.getText() != null && !viewHolder.destinationCurrencyEditText.getText().toString().equals(""))
            transactionEntity.desCurrency = viewHolder.destinationCurrencyEditText.getText().toString();
        if(viewHolder.txOriginEditText.getText() != null && !viewHolder.txOriginEditText.getText().toString().equals(""))
            transactionEntity.txOrigin = viewHolder.txOriginEditText.getText().toString();
        if(viewHolder.personalIDEditText.getText() != null && !viewHolder.personalIDEditText.getText().toString().equals(""))
            transactionEntity.personalID = viewHolder.personalIDEditText.getText().toString();

        return transactionEntity;
    }


}
