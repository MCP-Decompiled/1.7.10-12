package net.minecraft.util;

public interface IProgressUpdate
{
    void displaySavingString(String p_73720_1_);

    void resetProgressAndMessage(String p_73721_1_);

    void displayLoadingString(String p_73719_1_);

    void setLoadingProgress(int p_73718_1_);

    void setDoneWorking();
}
